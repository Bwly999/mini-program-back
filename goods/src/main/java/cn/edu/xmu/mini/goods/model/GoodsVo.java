package cn.edu.xmu.mini.goods.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class GoodsVo {
    @NotBlank
    private String name;

    @NotBlank
    private String coverImgUrl;
    /**
     * 价格 单位为分
     */
    @Min(0)
    private Integer price;

    @Min(0)
    private Integer discountPrice;
    /**
     * 详细描述图片url列表
     */
    private List<String> descImgUrlList;

    private String originPlace;
    /**
     * 等级
     */
    private String level;

    private Integer weight;
    /**
     * 描述
     */
    private String desc;

    /**
     * 所属商铺Id
     */
    private String shopId;
}
