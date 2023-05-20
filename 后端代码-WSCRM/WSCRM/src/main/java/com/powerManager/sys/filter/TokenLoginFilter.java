package com.powerManager.sys.filter;

import com.alibaba.fastjson.JSON;
import com.powerManager.common.constant.SystemConstant;
import com.powerManager.common.result.JWTUtils;
import com.powerManager.sys.entity.SysUser;
import com.powerManager.sys.entity.SysUserRe;
import com.powerManager.sys.service.impl.UserDetailServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

//jyc done

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    private boolean doRegister = false;
    private SysUserRe sur = null;

    /**
     * 具体认证的方法
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        SysUserRe sysUser = null;
        // 前后端分离的项目中我们提交的数据是JSON字符串。不是表单提交的
        try {
            String loginInfo = getRequestJSON(request);

            sysUser = JSON.parseObject(loginInfo, SysUserRe.class);

            sur = sysUser;

            //判断条件(若repassword为空，代表登录，否则代表注册）
            if(sysUser.getRepassword().equals("")){
                //test
                System.out.println("登录————————————————————————————————————");
                doRegister = false;
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(sysUser.getUsername(),sysUser.getPassword());
            return authenticationManager.authenticate(authenticationToken);
            }
            else{
                System.out.println("注册————————————————————————————————————");
                doRegister = true;
                if(!sysUser.getRepassword().equals(sysUser.getPassword())){
                    response.setContentType("application/json;charset=utf-8");

                    response.setStatus(HttpServletResponse.SC_OK);
                    PrintWriter writer = response.getWriter();
                    Map<String,Object> resultMap = new HashMap<>();
                    response.setContentType("application/json;charset=utf-8");
                    resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                    resultMap.put("msg","密码不一致!");
                    writer.write(JSON.toJSONString(resultMap));
                    writer.flush();
                    writer.close();
                }
                else{
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(sysUser.getUsername(),sysUser.getPassword());
                    return authenticationManager.authenticate(authenticationToken);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getRequestJSON(HttpServletRequest request) throws IOException {
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String inputStr = null;
        while((inputStr = streamReader.readLine() ) != null){
            sb.append(inputStr);
        }
        return sb.toString();
    }

    /**
     * 登录成功的方法
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // 生成Token信息
        Map<String,String> map = new HashMap<>();
        map.put("username",authResult.getName());
        // TODO 还可以存储当前用户具有的角色
        // 生成对应的Token信息
        String token = JWTUtils.getToken(map);
        // 需要把生成的Token信息响应给客户端
        response.addHeader("Authorization", SystemConstant.SYS_TOKEN_PREFIX +token);
        response.addHeader("Access-Control-Expose-Headers","Authorization");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("code", HttpServletResponse.SC_OK);
        resultMap.put("msg","认证通过");
        writer.write(JSON.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    /**
     * 登录失败的方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");

        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        Map<String,Object> resultMap = new HashMap<>();
        if(doRegister){

            if(UserDetailServiceImpl.doExistence){
                resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
                resultMap.put("msg","账号已存在!");
            }
            else{
                System.out.println(UserDetailServiceImpl.doExistence + " ____________ "+sur.getUsername());
                //注册成功，在数据库写入账号
                SysUser su=new SysUser();
                su.setUsername(sur.getUsername());
                su.setPassword(sur.getPassword());
                //su.setUserId();
                //todo//???

                resultMap.put("code", HttpServletResponse.SC_OK);
                resultMap.put("msg","注册成功!");
            }
        }
        else{
            resultMap.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            resultMap.put("msg","用户名或密码错误!");
        }
        writer.write(JSON.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

}
