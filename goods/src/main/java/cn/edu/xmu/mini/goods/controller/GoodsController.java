package cn.edu.xmu.mini.goods.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.goods.model.GoodsVo;
import cn.edu.xmu.mini.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
@Validated
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/")
    public Object getGoods(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                           @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<Goods> goodsPage = goodsService.getGoodsUsePage(page, pageSize);
        return Common.decorateReturnObject(new ReturnObject(goodsPage));
    }

    @PostMapping("/")
    public Object addGoods(@RequestBody GoodsVo goodsVo) {

    }
}
