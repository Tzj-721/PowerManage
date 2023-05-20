package com.powerManager.common.result;


import java.lang.annotation.*;

/**
 * @author 编码时长两年半
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IgnoredResultWrapper {
}
