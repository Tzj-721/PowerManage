package com.powerManager.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.powerManager.common.annotation.SystemLog;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysMail;
import com.powerManager.sys.mapper.SysMailMapper;
import com.powerManager.sys.model.SysMailQueryDTO;
import com.powerManager.sys.service.ISysMailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysMailServiceImpl extends ServiceImpl<SysMailMapper, SysMail> implements ISysMailService {
    @Override
    public void sendMail(SysMail sysMail) {
        sysMail.setCreateTime(LocalDateTime.now());
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        sysMail.setFrom_user_name(userName);
        //System.out.println(sysMail.getTo_role_name());
        this.save(sysMail);
    }

    @Override
    public PageUtils queryPage(SysMailQueryDTO queryDTO) {
        //获取当前登录的用户
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userName = (String) token.getPrincipal();
        System.out.println(userName);
        List<String> list = this.baseMapper.selectRoleNameByUserName(userName);
        QueryWrapper<SysMail> wrapper = new QueryWrapper<SysMail>()
                       .in("to_role_name",list)
                .like(StringUtils.isNotEmpty(queryDTO.getSubject()), "subject", queryDTO.getSubject());
        Page<SysMail> page = this.page(queryDTO.page(), wrapper);


        List<SysMail> records = page.getRecords();
        for (SysMail record : records) {
            record.setFrom_user_name(userName);
            //System.out.println(record);
        }
        page.setRecords(records);

        return new PageUtils(page);
    }

    @SystemLog("删除邮件")
    @Override
    public boolean deleteMailById(Long mailId) {
        this.baseMapper.deleteById(mailId);
        return true;
    }

    @Override
    public String showMail(Long mailId) {
        return this.baseMapper.showMailById(mailId);
    }
}
