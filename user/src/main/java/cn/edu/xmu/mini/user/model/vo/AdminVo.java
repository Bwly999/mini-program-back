package cn.edu.xmu.mini.user.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class AdminVo {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    @Pattern(regexp = "1\\d{10}", message = "手机号格式不正常")
    private String phone;

    @NotBlank
    private String shopName;
}
