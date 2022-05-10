package cn.edu.xmu.mini.goods.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
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
    /**
     * 详细描述图片url列表
     */
    private List<String> descImgUrlList;

    private GoodsDetail detail;

    /**
     * 所属商铺Id
     */
    private String shopId;

    @Data
    @Builder
    public static class GoodsDetail {
        private String originPlace;
        /**
         * 等级
         */
        private String level;
        private Integer weight;
        /**
         * 描述
         */
        private String desc;
    }
}
