package com.powerManager.sys.controller;



import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.powerManager.config.AliPayConfig;
import com.powerManager.sys.model.AliPayDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/toPay")
public class AliPayController {

    private static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    //签名方式
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @GetMapping("/pay")
    public String pay(AliPayDTO aliPayDTO) throws Exception{
        //System.out.println(aliPayConfig.toString());
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 2. 创建 Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", aliPayDTO.getTraceNo());  // 我们自己生成的订单编号
        bizContent.set("total_amount", aliPayDTO.getTotalAmount()); // 订单的总金额
        bizContent.set("subject", aliPayDTO.getSubject());   // 支付的名称
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());

        // 执行请求，拿到响应的结果，返回给浏览器
        String str = "";
        try {
            //System.out.println(request.getBizContent());
            //AlipayTradePagePayResponse response = alipayClient.execute(request);
            //str = alipayClient.pageExecute(request, "GET").getBody(); // 调用SDK生成表单
            AlipayTradePagePayResponse alipayTradePagePayResponse = alipayClient.pageExecute(request);
            str = alipayTradePagePayResponse.getBody();
            //.certificateExecute(request).getBody()

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(str);
        return str;
//        httpResponse.setContentType("text/html;charset=" + CHARSET);
//        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
//        httpResponse.getWriter().flush();
//        httpResponse.getWriter().close();

    }
}
