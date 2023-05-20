package com.powerManager.sys.service;

import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powerManager.sys.model.ShowMenu;
import com.powerManager.sys.model.SysMenuQueryDTO;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> listParent();

    PageUtils listPage(SysMenuQueryDTO dto);

    void saveOrUpdateMenu(SysMenu sysMenu);

    SysMenu queryMenuById(long menuId);

    String deleteMenuById(long menuId);

    List<Integer> queryMenuIdById(Long roleId);

    List<ShowMenu> getShowMenu();
}
