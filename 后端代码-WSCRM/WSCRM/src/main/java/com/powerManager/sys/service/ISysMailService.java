package com.powerManager.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysMail;
import com.powerManager.sys.model.SysMailQueryDTO;

public interface ISysMailService extends IService<SysMail> {
    void sendMail(SysMail sysMail);

    PageUtils queryPage(SysMailQueryDTO queryDTO);

    boolean deleteMailById(Long mailId);

    String showMail(Long mailId);
}
