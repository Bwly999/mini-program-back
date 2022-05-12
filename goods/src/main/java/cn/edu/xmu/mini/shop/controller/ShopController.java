package cn.edu.xmu.mini.shop.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.orders.model.GenerateOrderVo;
import cn.edu.xmu.mini.orders.model.Orders;
import cn.edu.xmu.mini.shop.dao.ShopDao;
import cn.edu.xmu.mini.shop.model.Shop;
import cn.edu.xmu.mini.shop.model.ShopVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/shop", produces = "application/json;charset=UTF-8")
@Validated
public class ShopController {

    @Autowired
    private ShopDao shopDao;
    //生成订单
    @PostMapping("/create")
    public Object create(@RequestBody ShopVo shopVo) {
        Shop shop = new Shop();
        BeanUtils.copyProperties(shopVo, shop);
        shopDao.insert(shop);
        return Common.decorateReturnObject(new ReturnObject());
    }

    //
    @GetMapping("/{userId}")
    public Object getShopByUserId(@PathVariable String userId) {
        Shop shop = Shop.builder()
                .userId(userId).build();
        return Common.decorateReturnObject(new ReturnObject(shopDao.findAll(Example.of(shop))));
    }
}
