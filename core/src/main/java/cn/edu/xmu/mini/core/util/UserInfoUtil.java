package cn.edu.xmu.mini.core.util;

import cn.edu.xmu.mini.core.constants.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Slf4j
public class UserInfoUtil {
    public static String getUserId() {
        try {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String payload = httpServletRequest.getHeader(SecurityConstants.JWT_PAYLOAD_KEY);


            String decodePayload = URLDecoder.decode(payload, "UTF-8");
            String userId = JacksonUtil.parseObject(decodePayload, "userId", String.class);
            return userId;
        } catch (UnsupportedEncodingException | NullPointerException e) {
            log.error("UserInfoUtil getUserId:" + e.getMessage());
            return null;
        }
    }

    public static String getShopId() {
        return getUserId();
    }
}
