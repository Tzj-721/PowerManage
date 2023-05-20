package com.powerManager.sys.model;

import com.powerManager.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SysRoleQueryDTO extends PageDTO {
    @ApiModelProperty("角色名称")
    private String roleName;
}
