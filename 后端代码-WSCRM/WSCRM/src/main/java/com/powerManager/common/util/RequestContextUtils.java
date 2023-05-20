package com.powerManager.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author 编码时长两年半
 * @date 2023-5-18
 */
public class RequestContextUtils {

    private RequestContextUtils() {
    }

    public static HttpServletRequest getRequestOrNull() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(requestAttributes1 -> ((ServletRequestAttributes) requestAttributes1).getRequest())
                .orElse(null);
    }

    public static Optional<HttpServletRequest> getOptionalRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(requestAttributes1 -> ((ServletRequestAttributes) requestAttributes1).getRequest());
    }

    public static String getRequestUri() {
        return getOptionalRequest().map(HttpServletRequest::getRequestURI).orElse("");
    }

    public static String getHeader(String header) {
        HttpServletRequest requestOrNull = getRequestOrNull();
        return requestOrNull == null ? null : requestOrNull.getHeader(header);
    }
}
