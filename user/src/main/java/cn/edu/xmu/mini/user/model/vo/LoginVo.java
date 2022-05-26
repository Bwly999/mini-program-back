package cn.edu.xmu.mini.user.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginVo {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
