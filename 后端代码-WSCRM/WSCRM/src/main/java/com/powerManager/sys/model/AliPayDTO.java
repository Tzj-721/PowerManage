package com.powerManager.sys.model;

import lombok.Data;

@Data
public class AliPayDTO {
    private String traceNo;
    private String totalAmount;
    private String subject;
    private String alipayTraceNo;
}
