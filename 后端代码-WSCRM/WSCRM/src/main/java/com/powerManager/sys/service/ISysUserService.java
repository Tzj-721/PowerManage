package com.powerManager.sys.service;

import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powerManager.sys.model.SysUserQueryDTO;

import java.util.List;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public interface ISysUserService extends IService<SysUser> {

    List<SysUser> queryByUserName(String username);

    boolean checkUserName(String username);

    PageUtils queryPage(SysUserQueryDTO dto);

    SysUser queryByUserId(Long userId);

    void saveOrUpdateUser(SysUser sysUser);
}
