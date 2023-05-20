package com.powerManager.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powerManager.common.annotation.SystemLog;
import com.powerManager.common.constant.SystemConstant;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysMenu;
import com.powerManager.sys.entity.SysRole;
import com.powerManager.sys.mapper.SysRoleMapper;
import com.powerManager.sys.model.SysRoleQueryDTO;
import com.powerManager.sys.service.ISysMenuService;
import com.powerManager.sys.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    ISysMenuService menuService;

    @Override
    public PageUtils queryPage(SysRoleQueryDTO queryDTO) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>().like(
                StringUtils.isNotEmpty(queryDTO.getRoleName()),"role_name",queryDTO.getRoleName()
        );
        Page<SysRole> page = this.page(queryDTO.page(), wrapper);

        return new PageUtils(page);
    }

    @SystemLog("添加或更新角色")
    @Override
    public void saveOrUpdateRole(SysRole role) {
        //同步维护角色和菜单的关系
        // 判断 角色编号是否存在，如果存在就走更新的逻辑否则新增数据
        if(role.getRoleId() != null && role.getRoleId() != 0){
            // 表示更新操作
            this.update(role);
            //删除角色已分配的菜单
            sysRoleMapper.deleteMenuByRoleId(role.getRoleId());

        }else{
            this.saveRole(role);
        }
        //新增分配的菜单信息
        List<Integer> menuIds = role.getMenuIds();
        for (Integer menuId : menuIds) {
            sysRoleMapper.insertRoleAndMenu(role.getRoleId(),menuId);
        }
    }

    public void saveRole(SysRole role){
        role.setCreateTime(LocalDateTime.now());
        this.save(role);
    }

    @Override
    public void update(SysRole role) {
        this.baseMapper.updateById(role);
    }

    @Override
    public void deleteBatch(Long[] roleIds) {

    }
    //查询角色分配的菜单
    @Override
    public Map<String, Object> dispatherRoleMenu(Long roleId) {
        List<SysMenu> parents = menuService.listParent();
        List<Map<String,Object>> list=new ArrayList<>();
        if(parents!=null&&parents.size()>0){
            for (SysMenu parent : parents) {
                Map<String,Object> map =new HashMap<>();
                map.put("id",parent.getMenuId());
                map.put("label",parent.getName());
                //根据父菜单编号查询对应子菜单信息
                Long parentId=parent.getMenuId();
                QueryWrapper<SysMenu> wrapper=new QueryWrapper<>();
                wrapper.eq("parent_id",parentId);
                List<SysMenu> subMenus = menuService.list(wrapper);
                List<Map<String,Object>> subList=new ArrayList<>();
                if(subMenus!=null&&subMenus.size()>0){
                    for (SysMenu subMenu : subMenus) {
                        Map<String,Object> subMap =new HashMap<>();
                        subMap.put("id",subMenu.getMenuId());
                        subMap.put("label",subMenu.getName());
                        subList.add(subMap);
                    }
                }
                map.put("children",subList);
                list.add(map);
            }
        }
        //根据角色编号查询已分配的菜单
        List<Integer> menuIds =menuService.queryMenuIdById(roleId);
        Map<String,Object> resMap =new HashMap<>();
        resMap.put("checks",menuIds);
        resMap.put("treeData",list);
        return resMap;
    }

    /**
     *
     * @param roleName
     * @return
     *    true 存在
     *    false 不存在
     */
    @Override
    public boolean checkRoleName(String roleName) {
        if(StringUtils.isEmpty(roleName)){
            return false;
        }
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<SysRole>().eq("role_name",roleName);
        int count = this.count(queryWrapper);
        return count > 0;
    }

    @SystemLog("删除角色")
    @Override
    public boolean deleteRoleById(Long roleId) {
        // 删除角色信息
        // 如果这个角色分配给了 用户或者角色绑定了菜单。那么都不允许删除角色
        // 查看该角色是否分配给了用户
        int count = this.baseMapper.checkRoleCanDelete(roleId);
        if(count == 0){
            // 表示可以删除
            this.baseMapper.deleteById(roleId);
        }
        return count == 0;
    }

    @Override
    public List<SysRole> queryByUserId(Long userId) {
        return sysRoleMapper.queryByUserId(userId);
    }



    @Override
    public void exportExcel() throws IOException {
        String fileName = "roles";
        //直接获取数据库的相应表的
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println(sysRoles);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("角色表");
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        createExcel(workbook,sheet,sysRoles);
        String path = "../ui/ws-ui/static";
        //        //6、写入本地
        String pathFile = path + File.separator + fileName + ".xlsx";
        FileOutputStream outputStream = new FileOutputStream(pathFile);
        workbook.write(outputStream);
        System.out.println(workbook);
        //7、关闭流
        outputStream.close();
    }

    private void createExcel (XSSFWorkbook workbook,XSSFSheet sheet,List<SysRole> list) throws IOException{

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        XSSFRow row = sheet.createRow(0);
        // 创建一个居中格式
        XSSFCellStyle style = workbook.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        int rowNum = 1;
        //设置每列标题
        String[] headers = {"用户ID", "用户名"};
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            XSSFCell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (SysRole sysRole: list ) {

            XSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(sysRole.getRoleId());   //设置单元格内容
            row1.createCell(1).setCellValue(sysRole.getRoleName());
            //row1.createCell(2).setCellValue(sysRole.getCreateUserId());
            //row1.createCell(3).setCellValue(sysRole.getCreateUserId());
            //row1.createCell(4).setCellValue(sysRole.getCreateTime().toString());
            //row1.createCell(5).setCellValue(sysRole.getRemark());

            rowNum++;
        }

    }
}
