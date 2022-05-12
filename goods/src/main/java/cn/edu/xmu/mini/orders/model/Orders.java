package cn.edu.xmu.mini.orders.model;

import cn.edu.xmu.mini.goods.model.Goods;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.MongoId;
@Builder
@Data
@NoArgsConstructor
public class Orders {
    /**
     * 订单id
     */
    @MongoId
    private String id;

    /**
     * 物流单号
     */
    private String logisticsNumber;

    /**
     * 是否为自取
     * 0 否
     * 1 是
     */
    private Integer isSelf;

    /**
     * 如果自取则为空
     */
    private String address;


    /**
     * 购买数量
     */
    private Integer payNumber;

    /**
     * 实付款
     */
    private Integer payAmount;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 店铺id
     */
    private String shopId;
    /**
     * 订单状态
     * 0 未支付，生成订单
     * 1 已支付，待收货
     * 2 已确认收货
     * 3 发起退款，待商家确认
     * 4 已退款
     */
    private Integer state;

    private Goods.Comment comment;
}
