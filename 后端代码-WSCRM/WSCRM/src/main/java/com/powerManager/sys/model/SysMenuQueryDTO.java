package com.powerManager.sys.model;

import com.powerManager.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SysMenuQueryDTO extends PageDTO {
    @ApiModelProperty("菜单描述")
    private String label;
}
