package com.powerManager.sys.model;

import com.powerManager.sys.entity.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateDTO {
    private List<SysMenu> parents;
    private SysMenu sysMenu;
}
