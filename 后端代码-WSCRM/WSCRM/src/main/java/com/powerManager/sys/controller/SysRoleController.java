package com.powerManager.sys.controller;

import com.alipay.api.internal.util.file.IOUtils;
import com.powerManager.common.constant.SystemConstant;
import com.powerManager.common.util.PageUtils;
import com.powerManager.sys.entity.SysRole;
import com.powerManager.sys.model.SysRoleQueryDTO;
import com.powerManager.sys.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author 波哥
 * @since 2022-11-18
 */

@Api(tags = "系统角色",value = "SysRole")
@RestController
@RequestMapping("/sys/sysRole")
public class SysRoleController {

    @Autowired
    private ISysRoleService roleService;

    @ApiOperation(value = "查询分页角色",notes = "角色信息")
    @GetMapping("/list")
    public PageUtils list(@ApiParam(value = "查询的条件") SysRoleQueryDTO queryDTO){
        return roleService.queryPage(queryDTO);
    }


    @ApiOperation(value = "查询所有角色",notes = "角色信息")
    @GetMapping("/listAll")
    public List<SysRole> listAll(){
        return roleService.list();
    }


    @ApiOperation(value = "添加角色",notes = "添加角色")
    @PostMapping("/save")
    public String save(@RequestBody SysRole sysRole){
        roleService.saveOrUpdateRole(sysRole);

        return "success";
    }

    @GetMapping("/deleteRole")
    public String deleteRole(Long roleId){
        boolean flag = roleService.deleteRoleById(roleId);
        return flag?"1":"0";
    }

    /**
     * 检查角色名称是否存在
     * @param roleName
     * @return
     *
     */
    @ApiOperation(value = "检查角色名称是否存储",notes = "校验角色名称")
    @GetMapping("/checkRoleName")
    public String checkRoleName(String roleName){
        boolean flag = roleService.checkRoleName(roleName);
        return flag?"success":"fail";
    }

    @ApiOperation(value = "查询分配的菜单信息",notes = "查询分配的菜单信息")
    @GetMapping("/dispatherRoleMenu")
    public Map<String,Object> dispatherRoleMenu(Long roleId){
        return roleService.dispatherRoleMenu(roleId);
    }


    @ApiOperation("导出excel")
    @GetMapping("/excel")
    public void excel(HttpServletResponse response) {
        String fileName = "用户信息";
        OutputStream os = null;
        InputStream is= null;
        try {
            //首先调用业务方法得出相应的文件
            roleService.exportExcel();
            // 取得输出流
            os = response.getOutputStream();
            // 清空输出流
            response.reset();
            response.setContentType("application/x-download;charset=utf-8");
            // 将响应头中的Content-Disposition暴露出来，不然前端获取不到
            //response.setHeader("Access-Control-Allow-Origin", "Content-Disposition");
            //Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，
            // 保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
            //把文件名按UTF-8取出，并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("utf-8"),"ISO8859-1"));
            //读取流
            File f = new File(SystemConstant.ROOT_PATH +fileName);
            is = new FileInputStream(f);
            if (is == null) {
                System.out.println("下载附件失败，请检查文件“" + fileName + "”是否存在");
                return;
            }
            //复制
            IOUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            return;
        }
        //文件的关闭放在finally中
        finally
        {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //其实，这个返回什么都不重要
        return;
    }
}
