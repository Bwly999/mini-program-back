package cn.edu.xmu.mini.user.model.weixin;

import lombok.Data;

@Data
public class Session {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    private String errmsg;
}
