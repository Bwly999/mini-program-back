package cn.edu.xmu.mini.goods.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    @Id
    private String id;
    private String name;
    private String coverImgUrl;

    private String category;

    /**
     * 1 上架
     * 0 下架
     */
    private Integer state;

    /**
     * 商品视频
     */
    private String videoUrl;

    /**
     * 视频后面的滑动图片
     */
    private List<String> scollImages;
    /**
     * 月销量
     */
    private Integer monthSale;

    /**
     * 价格 单位为分
     */
    private Integer price;
    private Integer discountPrice;

    /**
     * 库存
     */
    private Integer stock;
    /**
     * 详细描述图片url列表
     */
    private List<String> detailImgUrlList;

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
     * 所属商铺Id, 默认商铺Id为对应admin的Id
     */
    private String shopId;

    private String shopName;

    private List<Comment> commentList;

    private Boolean deleted;

    @Data
    public static class Comment {
        private String userId;
        private String avatarUrl;
        private String nickName;
        private Integer rate;
        @CreatedDate
        private LocalDateTime createdTime;
        private String content;
    }
}
