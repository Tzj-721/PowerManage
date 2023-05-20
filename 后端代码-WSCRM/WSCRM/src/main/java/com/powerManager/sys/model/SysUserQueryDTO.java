package com.powerManager.sys.model;

import com.powerManager.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysUserQueryDTO extends PageDTO {
    @ApiModelProperty("用户名称")
    private String username;
}
