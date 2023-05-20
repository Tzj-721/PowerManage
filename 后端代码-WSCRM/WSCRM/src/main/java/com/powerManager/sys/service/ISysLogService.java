package com.powerManager.sys.service;

import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.powerManager.sys.model.SysLogQueryDTO;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public interface ISysLogService extends IService<SysLog> {

    PageUtils listPage(SysLogQueryDTO dto);
}
