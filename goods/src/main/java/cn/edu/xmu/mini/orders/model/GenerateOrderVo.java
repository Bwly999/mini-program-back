package cn.edu.xmu.mini.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateOrderVo {
    private String userId;
    private String goodsId;
    private Integer payNumber;
    private Integer payAmount;
    private Integer isSelf;
    private String address;
    private String shopId;
}
