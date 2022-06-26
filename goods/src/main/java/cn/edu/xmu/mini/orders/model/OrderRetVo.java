package cn.edu.xmu.mini.orders.model;

import cn.edu.xmu.mini.goods.model.Goods;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class OrderRetVo {
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
     * 支付方式
     * 0 微信支付
     * 1 线下到付，自取
     */
    private Integer payWay;

    /**
     * 如果自取则为空
     */
    private Address address;

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

    private SimpleGoodsRetVo goods;

    private String shopId;

    private String shopName;

    private Integer state;

    private Goods.Comment comment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SimpleGoodsRetVo {
        private String id;
        private String name;
        private String coverImgUrl;
    }
}
