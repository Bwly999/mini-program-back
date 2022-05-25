package cn.edu.xmu.mini.user.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.user.exception.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExecptionHandler {
    @ExceptionHandler(value = UnAuthenticatedException.class)
    public Object handleUnAuthException(Exception e) {
        log.error(e.getStackTrace()[0] + "\n" + e.getMessage());
        return Common.decorateReturnObject(new ReturnObject(ReturnNo.AUTH_NEED_LOGIN, e.getMessage()));
    }
}
