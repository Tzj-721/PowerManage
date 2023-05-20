package com.powerManager.sys.controller;

import com.powerManager.common.constant.SystemConstant;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysRole;
import com.powerManager.sys.entity.SysUser;
import com.powerManager.sys.model.SysUserQueryDTO;
import com.powerManager.sys.service.ISysRoleService;
import com.powerManager.sys.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统用户 前端控制器
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
@Api(tags = "系统用户",value = "SysUser")
@RestController
@RequestMapping("/sys/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "查询系统用户",notes = "查询用户")
    @GetMapping("/list")
    public PageUtils list(SysUserQueryDTO dto){
        return userService.queryPage(dto);
    }

    @GetMapping("/queryUserById")
    public Map<String,Object> queryUserById(Long userId) {
        //根据id查询用户信息
        SysUser sysUser = userService.queryByUserId(userId);
        //查询所有的角色信息
        List<SysRole> roles = roleService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("roles", roles);
        map.put("user", sysUser);
        return map;
    }

    @ApiOperation(value = "检查账号是否存在",notes = "检查账号是否存在")
    @GetMapping("/checkUserName")
    public String checkUserName(String username){
        boolean checkFlag = userService.checkUserName(username);
        return checkFlag? SystemConstant.CHECK_SUCCESS : SystemConstant.CHECK_FAIL;
    }

    @ApiOperation(value = "添加账号",notes = "添加账号")
    @PostMapping("/save")
    public String save(@RequestBody SysUser sysUser){
        userService.saveOrUpdateUser(sysUser);
        return "success";
    }
}
