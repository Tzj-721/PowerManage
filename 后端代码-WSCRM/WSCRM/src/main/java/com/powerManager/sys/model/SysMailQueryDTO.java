package com.powerManager.sys.model;

import com.powerManager.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMailQueryDTO extends PageDTO {
    @ApiModelProperty("主题")
    private String subject;
}
