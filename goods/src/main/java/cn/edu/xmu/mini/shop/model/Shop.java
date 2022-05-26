package cn.edu.xmu.mini.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Shop {
    /**
     * 商铺id
     */
    @Id
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
