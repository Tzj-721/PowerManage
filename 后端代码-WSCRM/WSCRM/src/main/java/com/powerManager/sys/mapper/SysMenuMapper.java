package com.powerManager.sys.mapper;

import com.powerManager.sys.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.powerManager.sys.model.ShowMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    int canBeDeleted(@Param("menuId") long menuId);

    List<Integer> queryMenuIdById(@Param("roleId") Long roleId);

    List<SysMenu> selectShowMenuParentByUserName(@Param("userName") String userName);

    List<ShowMenu> selectShowMenuSubByUserName(@Param("userName") String userName,@Param("menuId") Long menuId);
}
