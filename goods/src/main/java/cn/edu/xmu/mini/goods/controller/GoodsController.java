package cn.edu.xmu.mini.goods.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.core.util.storage.StorageUtil;
import cn.edu.xmu.mini.goods.dao.GoodsDao;
import cn.edu.xmu.mini.goods.model.ChangeStockVo;
import cn.edu.xmu.mini.goods.model.CommentVo;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.goods.model.GoodsVo;
import cn.edu.xmu.mini.goods.service.GoodsService;
import cn.edu.xmu.mini.orders.model.GenerateOrderVo;
import cn.edu.xmu.mini.orders.model.Orders;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.websocket.server.PathParam;
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

    //修改库存
    @PostMapping("/changestock/{goodsId}")
    public Object changestock(@PathVariable String goodsId,@RequestBody ChangeStockVo changeStockVo) {
        Criteria criteria = Criteria.where("id").is(goodsId);
        Update update = new Update().set("stock",changeStockVo);
        mongoTemplate.updateFirst(new Query(criteria), update, Orders.class);

        return Common.decorateReturnObject(new ReturnObject());
    }

    // 用户api
    //根据名称查询商品
    @GetMapping("/")
    public Object getGoods(@RequestParam String name, @RequestParam String category,@RequestParam Integer lowPrice, @RequestParam Integer highPrice,
                                 @RequestParam(defaultValue = "1") @Min(1) Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Goods> goodsPage = goodsService.getGoods(name, category, lowPrice, highPrice, page, pageSize);
        return Common.decorateReturnObject(new ReturnObject(goodsPage));
    }
//
//    @GetMapping("/")
//    public Object getGoods(@RequestParam(defaultValue = "1") @Min(1) Integer page,
//                           @RequestParam(defaultValue = "10") Integer pageSize) {
//
//        Page<Goods> goodsPage = goodsService.getGoodsUsePage(page, pageSize);
//        return Common.decorateReturnObject(new ReturnObject(goodsPage));
//    }

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
            return List.of();
        }
        return Common.decorateReturnObject(new ReturnObject(goods.get()));
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
    @GetMapping("/shopId/{shopId}")
    public Object getGoodsByShopId(@PathVariable String shopId,
                                   @RequestParam(defaultValue = "1") @Min(1) Integer page,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Goods> goodsPage = goodsService.getGoodsByShopId(shopId, page, pageSize);
        return Common.decorateReturnObject(new ReturnObject(goodsPage));
    }
    @PostMapping("/")
    public Object addGoods(@RequestBody GoodsVo goodsVo) {
        Goods goods = goodsService.saveGoods(goodsVo);
        return Common.decorateReturnObject(new ReturnObject(goods));
    }


    @PostMapping("/img")
    @ApiOperation(value = "文件上传")
    public Object uploadImage(@RequestParam(value = "file") MultipartFile file) throws Exception {
        try {
            if (file == null) {
                return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
            }
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

            String path = storageUtil.store(file.getInputStream(), suffix);
            return Common.decorateReturnObject(new ReturnObject(path));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
