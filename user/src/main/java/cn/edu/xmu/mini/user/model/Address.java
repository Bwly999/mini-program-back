package cn.edu.xmu.mini.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotBlank(message = "收货人姓名不能为空")
    private String consignee;
    @NotBlank(message = "收货人电话不能为空")
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Boolean isDefault;
}
