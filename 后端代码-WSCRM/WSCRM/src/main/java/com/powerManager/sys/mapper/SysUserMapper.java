package com.powerManager.sys.mapper;

import com.powerManager.sys.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    void deleteRoleByUserId(@Param("userId") Long userId);

    void saveUserAndRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);

    List<Integer> selectRoleIdsByUserId(@Param("userId") Long userId);
}
