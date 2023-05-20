package com.powerManager.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powerManager.sys.entity.SysMail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMailMapper extends BaseMapper<SysMail> {
    List<String> selectRoleNameByUserName(@Param("userName") String userName);

    String showMailById(@Param("mailId") Long mailId);
}
