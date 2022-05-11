package cn.edu.xmu.mini.goods.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.core.util.storage.StorageUtil;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.goods.model.GoodsVo;
import cn.edu.xmu.mini.goods.service.GoodsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
@Validated
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StorageUtil storageUtil;

    // 用户api
    @GetMapping("/")
    public Object getGoods(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Goods> goodsPage = goodsService.getGoodsUsePage(page, pageSize);
        return Common.decorateReturnObject(new ReturnObject(goodsPage));
    }
    //商品评价
    @GetMapping("/goodscomment/{goodsId}")
    public Object getGoodsByShopId(@PathVariable String goodsId) {

        return Common.decorateReturnObject(new ReturnObject(goodsPage));
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
