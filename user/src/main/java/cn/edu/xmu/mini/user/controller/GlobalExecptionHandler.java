package cn.edu.xmu.mini.user.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.user.exception.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExecptionHandler {
    @ExceptionHandler(value = UnAuthenticatedException.class)
    public Object handleUnAuthException(Exception e) {
        log.error(e.getStackTrace()[0] + "\n" + e.getMessage());
        return Common.decorateReturnObject(new ReturnObject(ReturnNo.AUTH_NEED_LOGIN, e.getMessage(), null));
    }

    /**
     * 参数校验失败的异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public Object handleBindException(BindException e) {
        log.error(e.getStackTrace()[0] + "\n" + e.getMessage());
        return Common.decorateReturnObject(new ReturnObject(ReturnNo.FIELD_NOTVALID, e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";")), null));
    }

    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception e) {
        log.error(e.getStackTrace()[0] + "\n" + e.getMessage());
        return Common.decorateReturnObject(new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR, e.getMessage(), null));
    }
}
