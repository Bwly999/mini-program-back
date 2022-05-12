package cn.edu.xmu.mini.shop.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.MongoId;
@Builder
@NoArgsConstructor
public class Shop {
    /**
     * 商铺id
     */
    @MongoId
    private String id;
    /**
     * 商铺名称
     */
    private String name;

    /**
     * 所属用户id
     */
    private String userId;


}
