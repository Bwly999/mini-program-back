package cn.edu.xmu.mini.user.model;

import lombok.Data;

@Data
public class UserVo {
    private String code;
    private String openId;
    private String avatarUrl;
    private String city;
    private Integer gender;
    private String nickName;
    private String province;
}
