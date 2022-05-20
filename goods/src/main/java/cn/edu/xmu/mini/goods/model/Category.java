package cn.edu.xmu.mini.goods.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@NoArgsConstructor
public class Category {
    @MongoId
    private String id;
    private String name;
}
