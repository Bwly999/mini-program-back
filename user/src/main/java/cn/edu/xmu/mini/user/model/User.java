package cn.edu.xmu.mini.user.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Accessors(chain = true)
public class User {
    @MongoId
    private String id;
    /**
     * 小程序用户唯一标识
    */
    @Indexed
    private String openId;

    /**
     * 用户信息
     */
    private String avatarUrl;
    private String city;
    private Integer gender;
    private String nickName;
    private String province;
}
