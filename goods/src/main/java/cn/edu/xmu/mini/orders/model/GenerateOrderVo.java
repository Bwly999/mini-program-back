package cn.edu.xmu.mini.orders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GenerateOrderVo {
    @NotNull
    private String userId;
    @NotNull
    private String goodsId;
    @NotNull
    @Min(1)
    private Integer payNumber;
    private Integer payAmount;

    @NotNull
    @Min(value = 0, message = "支付方式不存在")
    @Max(value = 1, message = "支付方式不存在")
    private Integer payWay;
    private Integer isSelf;

    @NotNull
    private Address address;
}
