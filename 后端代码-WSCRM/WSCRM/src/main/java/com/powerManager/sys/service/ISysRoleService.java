package com.powerManager.sys.service;

import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powerManager.sys.model.SysRoleQueryDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public interface ISysRoleService extends IService<SysRole> {

    PageUtils queryPage(SysRoleQueryDTO queryDTO);

    void saveOrUpdateRole(SysRole role);

    void update(SysRole role);

    void deleteBatch(Long[] roleIds);

    boolean checkRoleName(String roleName);

    boolean deleteRoleById(Long roleId);

    List<SysRole> queryByUserId(Long userId);

    Map<String, Object> dispatherRoleMenu(Long roleId);

    void exportExcel() throws IOException;

}
