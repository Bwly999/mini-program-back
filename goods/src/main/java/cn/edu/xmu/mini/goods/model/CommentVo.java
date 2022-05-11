package cn.edu.xmu.mini.goods.model;

import lombok.Data;

@Data
public class CommentVo {
    private String userId;
    private String avatarUrl;
    private String nickName;
    private Integer rate;
    private String content;
}
