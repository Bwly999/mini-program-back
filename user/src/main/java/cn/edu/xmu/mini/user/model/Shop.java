package cn.edu.xmu.mini.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
public class Shop {
    private String name;

    private List<String> orderIdList;
}
