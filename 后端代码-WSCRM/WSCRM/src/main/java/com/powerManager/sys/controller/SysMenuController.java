package com.powerManager.sys.controller;

import com.powerManager.common.constant.SystemConstant;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysMenu;
import com.powerManager.sys.model.MenuUpdateDTO;
import com.powerManager.sys.model.ShowMenu;
import com.powerManager.sys.model.SysMenuQueryDTO;
import com.powerManager.sys.service.ISysMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
@Controller
@RequestMapping("/sys/sysMenu")
@RestController
public class SysMenuController {
    @Autowired
    ISysMenuService menuService;


    @ApiOperation(value = "查询父菜单信息",notes = "查询父菜单信息")
    @GetMapping("/listParent")
    public List<SysMenu> listParent(){
        return menuService.listParent();
    }


    @ApiOperation(value = "查询菜单信息",notes = "查询菜单信息")
    @GetMapping("/list")
    public PageUtils list(SysMenuQueryDTO dto){
        return menuService.listPage(dto);
    }
    @ApiOperation(value = "操作菜单数据",notes = "添加、更新菜单数据")
    @PostMapping("/save")
    public String save(@RequestBody SysMenu sysMenu){
        if(sysMenu!=null){
            menuService.saveOrUpdateMenu(sysMenu);
        }
        return SystemConstant.CHECK_SUCCESS;
    }
    @ApiOperation(value = "根据ID查询菜单",notes = "根据ID查询菜单")
    @GetMapping("/queryMenuById")
    public MenuUpdateDTO queryMenuById(long menuId){
        SysMenu sysMenu = menuService.queryMenuById(menuId);
        List<SysMenu> parents = menuService.listParent();
        return new MenuUpdateDTO(parents,sysMenu);
    }
    @ApiOperation(value = "删除菜单信息",notes = "删除菜单信息")
    @GetMapping("/deleteMenu")
    public String deleteMenu(long menuId){
        return menuService.deleteMenuById(menuId);
    }

    @GetMapping("/getShowMenu")
    public List<ShowMenu> getShowMenu(){
        return menuService.getShowMenu();
    }
}
