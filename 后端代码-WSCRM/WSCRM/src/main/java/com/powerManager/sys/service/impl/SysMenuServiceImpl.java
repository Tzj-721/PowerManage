package com.powerManager.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powerManager.common.annotation.SystemLog;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysMenu;
import com.powerManager.sys.mapper.SysMenuMapper;
import com.powerManager.sys.model.ShowMenu;
import com.powerManager.sys.model.SysMenuQueryDTO;
import com.powerManager.sys.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Autowired
    private SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> listParent() {
        //列出所有父菜单，用于新建菜单时供其选择相应的父菜单
        return this.baseMapper.selectList(new QueryWrapper<SysMenu>().eq("parent_id", 0));
    }

    @Override
    public List<Integer> queryMenuIdById(Long roleId) {
        return menuMapper.queryMenuIdById(roleId);
    }

    @Override
    public PageUtils listPage(SysMenuQueryDTO dto) {
        //查询一级菜单
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0)
                .like(StringUtils.isNotBlank(dto.getLabel()), "label", dto.getLabel())
                .orderByAsc("order_num");
        Page<SysMenu> page = this.page(dto.page(), wrapper);
        List<SysMenu> p_menus = page.getRecords();

        //查询二级菜单
        List<SysMenu> menus = p_menus.stream().map(item -> {
            Long menuId = item.getMenuId();
            //判断该菜单是否可以被删除
            int count = menuMapper.canBeDeleted(menuId);
            if (count == 0) {
                item.setCanBeDeleted(true);
            } else {
                item.setCanBeDeleted(false);
            }

            QueryWrapper<SysMenu> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("parent_id", menuId)
                    .orderByAsc("order_num");
            List<SysMenu> subMenus = this.baseMapper.selectList(wrapper1);
            //检查子菜单是否被分配
            for (SysMenu subMenu : subMenus) {
                int i = menuMapper.canBeDeleted(menuId);
                if (i == 0) {
                    subMenu.setCanBeDeleted(true);
                } else {
                    subMenu.setCanBeDeleted(false);
                }
            }
            item.setChildren(subMenus);
            return item;
        }).collect(Collectors.toList());
        page.setRecords(menus);
        return new PageUtils(page);
    }

    @Override
    public SysMenu queryMenuById(long menuId) {
        return this.baseMapper.selectById(menuId);
    }

    @SystemLog("菜单添加或更新")
    @Override
    public void saveOrUpdateMenu(SysMenu sysMenu) {
        if (sysMenu.getMenuId() > 0) {
            this.updateById(sysMenu);
        } else {
            if (StringUtils.isNotBlank(sysMenu.getLabel()) && StringUtils.isBlank(sysMenu.getName())) {
                sysMenu.setName(sysMenu.getLabel());
            }

            if (sysMenu.getParentId() == null) {
                sysMenu.setParentId(0l);
            }
            this.save(sysMenu);
        }

    }

    @Override
    public List<ShowMenu> getShowMenu() {
        //获取当前登录的用户
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        //查询当前登录用户的所有的父菜单
        List<SysMenu> list = this.baseMapper.selectShowMenuParentByUserName(userName);
        if (list != null && list.size() > 0) {
            return list.stream().map(item -> {
                        ShowMenu showMenu = new ShowMenu();
                        showMenu.setIcon(item.getIcon());
                        showMenu.setUrl(item.getUrl());
                        showMenu.setPath(item.getPath());
                        showMenu.setLabel(item.getLabel());
                        showMenu.setName(item.getName());
                        //获取对应的子菜单

                        List<ShowMenu> subList = this.baseMapper.selectShowMenuSubByUserName(userName, item.getMenuId());
                        if (subList != null && subList.size() > 0) {
                            List<ShowMenu> showMenus = new ArrayList<>();
                            for (ShowMenu submenu : subList) {
                                ShowMenu sysMenu = new ShowMenu();
                                submenu.setIcon(sysMenu.getIcon());
                                submenu.setUrl(sysMenu.getUrl());
                                submenu.setPath(sysMenu.getPath());
                                submenu.setLabel(sysMenu.getLabel());
                                submenu.setName(sysMenu.getName());
                                showMenus.add(submenu);
                            }
                            showMenu.setChildren(showMenus);
                        }
                        return showMenu;
                    }
            ).collect(Collectors.toList());
        }
        return null;
    }

    @SystemLog("菜单删除")
    @Override
    public String deleteMenuById(long menuId) {
        //1.判断能否删除数据
        int count = menuMapper.canBeDeleted(menuId);
        if (count == 0) {//数据可以删除
            this.baseMapper.deleteById(menuId);
            return "1";
        }
        else return "0";
    }
}
