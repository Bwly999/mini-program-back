package cn.edu.xmu.mini.user.exception;

/**
 *  未认证异常
 */
public class UnAuthenticatedException extends Exception {
    public UnAuthenticatedException() {}
    public UnAuthenticatedException(String msg) {
        super(msg);
    }
}
