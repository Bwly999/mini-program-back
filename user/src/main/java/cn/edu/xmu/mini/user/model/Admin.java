package cn.edu.xmu.mini.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @MongoId
    private String id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    
    @NotNull
    @Pattern(regexp = "1\\d{10}", message = "手机号格式不正常")
    private String phone;

    @NotNull
    private Shop shop;
}
