package cn.edu.xmu.mini.goods.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.MongoUtils;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.core.util.storage.StorageUtil;
import cn.edu.xmu.mini.goods.config.CategoryProperty;
import cn.edu.xmu.mini.goods.dao.GoodsDao;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.goods.model.vo.ChangeStockVo;
import cn.edu.xmu.mini.goods.model.vo.CommentVo;
import cn.edu.xmu.mini.goods.model.vo.GoodsRetVo;
import cn.edu.xmu.mini.goods.model.vo.GoodsVo;
import cn.edu.xmu.mini.goods.service.GoodsService;
import cn.edu.xmu.mini.orders.model.Orders;
import cn.edu.xmu.mini.user.model.Admin;
import cn.edu.xmu.mini.user.model.Shop;
import com.mongodb.client.result.UpdateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
@Validated
public class GoodsController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StorageUtil storageUtil;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private CategoryProperty categoryProperty;

    //修改库存
    @PostMapping("/changestock/{goodsId}")
    public Object changestock(@PathVariable String goodsId,@RequestBody ChangeStockVo changeStockVo) {
        Criteria criteria = Criteria.where("id").is(goodsId);
        Update update = new Update().set("stock",changeStockVo);
        mongoTemplate.updateFirst(new Query(criteria), update, Orders.class);

        return Common.decorateReturnObject(new ReturnObject());
    }

    // 用户api

    /**
     * 查询商品
     * @param name
     * @param category
     * @param lowPrice
     * @param highPrice
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("")
    public Object getGoods(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String category,
                           @RequestParam(required = false) Integer lowPrice,
                           @RequestParam(required = false) Integer highPrice,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Goods> goodsPage = goodsService.getGoods(name, category, lowPrice, highPrice, page, pageSize);
        return Common.decorateReturnObject(new ReturnObject(goodsPage));
    }

    /**
     * 上架
     * @param goodsId
     * @return
     */
    @PostMapping("/online/{goodsId}")
    public Object online(@PathVariable String goodsId) {
        Criteria criteria = Criteria.where("id").is(goodsId);
        Update update = new Update().set("state",1);
        mongoTemplate.updateFirst(new Query(criteria), update, Goods.class);
        return Common.decorateReturnObject(new ReturnObject());
    }

    /**
     * 下架
     * @param goodsId
     * @return
     */
    @PostMapping("/offline/{goodsId}")
    public Object offline(@PathVariable String goodsId) {
        Criteria criteria = Criteria.where("id").is(goodsId);
        Update update = new Update().set("state",0);
        mongoTemplate.updateFirst(new Query(criteria), update, Goods.class);
        return Common.decorateReturnObject(new ReturnObject());
    }

    @GetMapping("/{goodsId}")
    public Object getGoodsById(@PathVariable String goodsId) {

        Optional<Goods> goods = goodsDao.findById(goodsId);
        if (goods.isEmpty()) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
        }

        GoodsRetVo goodsRetVo = new GoodsRetVo();
        BeanUtils.copyProperties(goods.get(), goodsRetVo);

        Admin admin = mongoTemplate.findById(goods.get().getShopId(), Admin.class);
        String shopName = Optional.ofNullable(admin).map(Admin::getShop).map(Shop::getName).orElse(null);
        goodsRetVo.setShopName(shopName);
        return Common.decorateReturnObject(new ReturnObject(goodsRetVo));
    }

    /**
     * 修改商品
     * @param goodsId
     * @param goodsVo
     * @return
     */
    @PutMapping("/{goodsId}")
    public Object changeGoods(@PathVariable String goodsId, @Valid @RequestBody GoodsVo goodsVo) {
        Update update = MongoUtils.getUpdateByObj(goodsVo);

        UpdateResult result = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(goodsId)), update, Goods.class);
        if (result.getMatchedCount() == 0L) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
        }
        return Common.decorateReturnObject(new ReturnObject());
    }

    // 查询商品评价
    @GetMapping("/{goodsId}/comment/")
    public Object getGoodsComment(@PathVariable String goodsId) {
        List<Goods.Comment> goodsComment = goodsService.getGoodsComment(goodsId);
        return Common.decorateReturnObject(new ReturnObject(goodsComment));
    }

    // 发表商品评价
    @PostMapping("{goodsId}/comment/")
    public Object postComment(@PathVariable String goodsId, @RequestBody CommentVo commentVo) {
        goodsService.postComment(goodsId, commentVo);
        return Common.decorateReturnObject(new ReturnObject());
    }

    // 商家api

    /**
     * 查询店铺所有商品
     * @param shopId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/shopId/{shopId}")
    public Object getGoodsByShopId(@PathVariable String shopId,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String category,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Goods> goodsPage = goodsService.getGoodsByShopId(shopId, name, category, page, pageSize);
        return Common.decorateReturnObject(new ReturnObject(goodsPage));
    }

    /**
     * 新增商品
     * @param goodsVo
     * @return
     */
    @PostMapping("")
    public Object addGoods(@RequestBody GoodsVo goodsVo) {
        Goods goods = goodsService.saveGoods(goodsVo);
        return Common.decorateReturnObject(new ReturnObject(goods));
    }

    /**
     * 删除商品
     * @param goodsId 商品Id
     * @return
     */
    @DeleteMapping("/{goodsId}")
    public Object deleteGoods(@PathVariable String goodsId) {
        Update update = new Update();
        update.set("deleted", true);

        UpdateResult result = mongoTemplate.updateFirst(new Query(Criteria.where("id").is(goodsId)), update, Goods.class);
        if (result.getMatchedCount() == 0L) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
        }
        return Common.decorateReturnObject(new ReturnObject());
    }

    @Value("${wishes.storage.webdav.replaceStr}")
    private String replaceStr;

    @PostMapping("/file")
    @ApiOperation(value = "文件上传")
    public Object uploadImage(@RequestParam(value = "file") MultipartFile file) throws Exception {
        try {
            if (file == null) {
                return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
            }
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            String path = storageUtil.store(file.getInputStream(), suffix);
            path = path.replace(replaceStr, "");
            return Common.decorateReturnObject(new ReturnObject(path));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/category")
    @ApiOperation("获取所有分类")
    public Object listAllCategory() {
//        List<Category> categoryList = mongoTemplate.findAll(Category.class);
        return Common.decorateReturnObject(new ReturnObject(categoryProperty.getCategoryList()));
    }
}
