package cn.edu.xmu.mini.orders.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.goods.dao.GoodsDao;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.orders.dao.OrdersDao;
import cn.edu.xmu.mini.orders.model.GenerateOrderVo;
import cn.edu.xmu.mini.orders.model.Orders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/orders", produces = "application/json;charset=UTF-8")
@Validated
public class OrdersController {
    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/{ordersId}")
    public Object getOrdersById(@PathVariable String ordersId) {

        Optional<Orders> orders = ordersDao.findById(ordersId);
        if (orders.isEmpty()) {
            return List.of();
        }
        return Common.decorateReturnObject(new ReturnObject(orders.get()));
    }

    @GetMapping("/{userId}")
    public Object getOrdersByUserId(@PathVariable String userId) {

        Orders order = Orders.builder()
                .userId(userId).build();
        return Common.decorateReturnObject(new ReturnObject(ordersDao.findAll(Example.of(order))));
    }

    /**
     * @param
     * @return
     */
    @GetMapping("/{shopId}")
    public Object getOrdersByShopId(@PathVariable String shopId) {
        Orders order = Orders.builder()
                .shopId(shopId).build();
        return Common.decorateReturnObject(new ReturnObject(ordersDao.findAll(Example.of(order))));
    }

    //生成订单
    @PostMapping("/create")
    public Object create(@RequestBody GenerateOrderVo generateOrderVo) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(generateOrderVo, orders);
        orders.setState(0);
        Orders order = ordersDao.insert(orders);
        return Common.decorateReturnObject(new ReturnObject(order));
    }

    //支付

    /**
     * 如果线下到付，则商家自行调用此接口
     * @param orderId
     * @return
     */
    @PostMapping("/pay/{orderId}")
    public Object pay(@PathVariable String orderId) {
        Criteria criteria = Criteria.where("id").is(orderId);
        Update update = new Update().set("state",1);
        mongoTemplate.updateFirst(new Query(criteria), update, Orders.class);

        Optional<Orders> orders = ordersDao.findById(orderId);
        if (orders.isEmpty()) {
            return List.of();
        }
        String goodsId = orders.get().getGoodsId();

        Optional<Goods> goods = goodsDao.findById(goodsId);
        if (goods.isEmpty()) {
            return List.of();
        }
        Criteria criteria1 = Criteria.where("id").is(goodsId);
        Update update1 = new Update().set("monthSale",goods.get().getMonthSale());
        mongoTemplate.updateFirst(new Query(criteria1), update1, Goods.class);

        return Common.decorateReturnObject(new ReturnObject());
    }

    //确认收货
    @PostMapping("/confirm/{orderId}")
    public Object confirm(@PathVariable String orderId) {
        Criteria criteria = Criteria.where("id").is(orderId);
        Update update = new Update().set("state",2);
        mongoTemplate.updateFirst(new Query(criteria), update, Orders.class);
        return Common.decorateReturnObject(new ReturnObject());
    }

    //客户发起退款
    @PostMapping("/refund/{orderId}")
    public Object refund(@PathVariable String orderId) {
        Criteria criteria = Criteria.where("id").is(orderId);
        Update update = new Update().set("state",3);
        mongoTemplate.updateFirst(new Query(criteria), update, Orders.class);
        return Common.decorateReturnObject(new ReturnObject());
    }

    //商家接受退款
    @PostMapping("/acceptrefund/{orderId}")
    public Object acceptrefund(@PathVariable String orderId) {
        Criteria criteria = Criteria.where("id").is(orderId);
        Update update = new Update().set("state",4);
        mongoTemplate.updateFirst(new Query(criteria), update, Orders.class);
        return Common.decorateReturnObject(new ReturnObject());
    }


}
