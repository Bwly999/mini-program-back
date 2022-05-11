package cn.edu.xmu.mini.goods.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.core.util.storage.StorageUtil;
import cn.edu.xmu.mini.goods.model.CommentVo;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.goods.model.GoodsQueryVo;
import cn.edu.xmu.mini.goods.model.GoodsVo;
import cn.edu.xmu.mini.goods.service.GoodsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
@Validated
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StorageUtil storageUtil;

    // 用户api
    //根据名称查询商品
    @GetMapping("/")
    public Object getGoods(@RequestParam String name, @RequestParam String category,
                                 @RequestParam(defaultValue = "1") @Min(1) Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Goods> goodsPage = goodsService.getGoods(name, category, page, pageSize);
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
