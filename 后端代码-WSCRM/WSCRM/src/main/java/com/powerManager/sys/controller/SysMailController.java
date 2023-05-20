package com.powerManager.sys.controller;

import com.powerManager.common.constant.SystemConstant;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysMail;
import com.powerManager.sys.model.SysMailQueryDTO;
import com.powerManager.sys.service.ISysMailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sys/sysMail")
@RestController
public class SysMailController {

    @Autowired
    ISysMailService mailService;

    @PostMapping("/save")
    public String save(@RequestBody SysMail sysMail){
        if(sysMail!=null){
            mailService.sendMail(sysMail);
        }
        return SystemConstant.CHECK_SUCCESS;
    }

    @ApiOperation(value = "查询分页邮件",notes = "邮件信息")
    @GetMapping("/list")
    public PageUtils list(@ApiParam(value = "查询的条件") SysMailQueryDTO queryDTO){
        return mailService.queryPage(queryDTO);
    }

    @GetMapping("/deleteMail")
    public String deleteMail(Long mailId){
        boolean flag = mailService.deleteMailById(mailId);
        return flag?"1":"0";
    }

    @ApiOperation(value = "根据ID查询邮件",notes = "根据ID查询邮件")
    @GetMapping("/showMail")
    public String showMail(Long mailId){
        return mailService.showMail(mailId);
    }

}
