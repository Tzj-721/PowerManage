package com.powerManager.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.powerManager.common.constant.SystemConstant;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysUser;
import com.powerManager.sys.mapper.SysUserMapper;
import com.powerManager.sys.model.SysUserQueryDTO;
import com.powerManager.sys.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统用户 服务实现类
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public List<SysUser> queryByUserName(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(username),"username",username);
        wrapper.eq("status", SystemConstant.USER_STATUS_NORMAL);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public void saveOrUpdateUser(SysUser sysUser){
        /**
         * 部分前面视频完成的代码，此处省略
         */
        if(sysUser.getUserId() > 0){
            // 更新
            this.updateById(sysUser);
        }else {
            // 添加
            sysUser.setCreateTime(LocalDateTime.now());
            sysUser.setCreateUserId(this.getCurrentUserID());
            // 密码加密处理
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password = encoder.encode(sysUser.getPassword());
            sysUser.setPassword(password);
            this.save(sysUser);
        }

        //完成用户分配的角色的保存
        if(sysUser.getUserId()>0){
            //删除当前更新用户分配的所有角色
            this.baseMapper.deleteRoleByUserId(sysUser.getUserId());
        }
        //添加分配的信息
        if(sysUser.getRoleIds()!=null&&sysUser.getRoleIds().size()>0){
            for (Integer roleId : sysUser.getRoleIds()) {
                this.baseMapper.saveUserAndRole(sysUser.getUserId(),roleId);
            }
        }
    }

    private List<SysUser> queryUser(SysUser user){
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(user.getUsername()),"username",user.getUsername())
                .eq(StringUtils.isNotBlank(user.getEmail()),"email",user.getEmail())
                .eq(StringUtils.isNotBlank(user.getMobile()),"mobile",user.getMobile())
                .eq(user.getUserId() != null && user.getUserId() > 0, "user_id", user.getUserId());
        return this.baseMapper.selectList(wrapper);
    }
    public Long getCurrentUserID(){
        // 设置添加数据的账号
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        List<SysUser> list = this.baseMapper.selectList(new QueryWrapper<SysUser>().eq("username", username));
        if (list != null && list.size() == 1){
            SysUser sysUser = list.get(0);
            return sysUser.getUserId();
        }
        return null;
    }
    @Override
    public SysUser queryByUserId(Long userId) {
        SysUser user=new SysUser();
        user.setUserId(userId);
        List<Integer> roleIds =this.baseMapper.selectRoleIdsByUserId(userId);
        user.setRoleIds(roleIds);
        return user;

    }
    @Override
    public boolean checkUserName(String username) {
        List<SysUser> list = baseMapper.selectList(new QueryWrapper<SysUser>().eq("username",username));
        if (list != null && list.size() > 0){
            return true;
            // 账号存在
        }
        return false;
        // 账号不存在
    }
    @Override
    public PageUtils queryPage(SysUserQueryDTO dto) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dto.getUsername()),"username",dto.getUsername());
        Page<SysUser> page = this.page(dto.page(), wrapper);
        return new PageUtils(page);
    }
}
