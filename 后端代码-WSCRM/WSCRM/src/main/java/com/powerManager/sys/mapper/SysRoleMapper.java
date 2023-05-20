package com.powerManager.sys.mapper;

import com.powerManager.sys.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    int checkRoleCanDelete(@Param("roleId") Long roleId);

    List<SysRole> queryByUserId(@Param("userId") Long userId);

    void deleteMenuByRoleId(@Param("roleId") Long roleId);

    void insertRoleAndMenu(@Param("roleId") Long roleId, @Param("menuId") Integer menuId);
}
