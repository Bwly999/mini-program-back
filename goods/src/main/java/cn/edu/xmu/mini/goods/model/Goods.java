package cn.edu.xmu.mini.goods.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class Goods {
    @MongoId
    private String id;
    private String name;
    private String coverImgUrl;
    /**
     * 价格 单位为分
     */
    private Integer price;
    private Integer discountPrice;

    private String category;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 详细描述图片url列表
     */
    private List<String> descImgUrlList;

    private String originPlace;
    /**
     * 等级
     */
    private String level;
    /**
     * 重量 单位 斤
     */
    private Integer weight;
    /**
     * 描述
     */
    private String desc;

    /**
     * 所属商铺Id
     */
    private String shopId;

    private List<Comment> commentList;

    @Data
    public static class Comment {
        private String userId;
        private String avatarUrl;
        private String nickName;
        private Integer rate;
        private LocalDateTime createTime;
        private String content;
    }
}
