package com.powerManager.sys.controller;

import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.model.SysLogQueryDTO;
import com.powerManager.sys.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */

@Controller
@RequestMapping("/sys/sysLog")
public class SysLogController {

    @Autowired
    private ISysLogService sysLogService;

    @GetMapping("/list")
    public PageUtils list(SysLogQueryDTO dto){
        PageUtils pageUtils = sysLogService.listPage(dto);
        return pageUtils;
    }
}
