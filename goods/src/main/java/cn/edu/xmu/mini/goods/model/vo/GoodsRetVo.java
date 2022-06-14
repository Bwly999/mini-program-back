package cn.edu.xmu.mini.goods.model.vo;

import cn.edu.xmu.mini.goods.model.Goods;
import lombok.Data;

import java.util.List;

@Data
public class GoodsRetVo {
    private String id;
    private String name;
    private String coverImgUrl;

    private String category;

    private Integer state;

    private String videoUrl;

    private List<String> scollImages;

    private Integer monthSale;

    private Integer price;
    private Integer discountPrice;

    private Integer stock;

    private List<String> detailImgUrlList;

    private String originPlace;

    private String level;

    private Integer weight;

    private String desc;

    private String shopId;

    private String shopName;

    private List<Goods.Comment> commentList;
}
