package cn.edu.xmu.mini.orders.controller;

import cn.edu.xmu.mini.core.aop.Audit;
import cn.edu.xmu.mini.core.aop.LoginUser;
import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.goods.dao.GoodsDao;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.orders.dao.OrdersDao;
import cn.edu.xmu.mini.orders.model.GenerateOrderVo;
import cn.edu.xmu.mini.orders.model.OrderRetVo;
import cn.edu.xmu.mini.orders.model.Orders;
import cn.edu.xmu.mini.user.model.Admin;
import cn.edu.xmu.mini.user.model.Shop;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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

    private OrderRetVo transformOrderToRetVo(Orders order) {
        OrderRetVo orderRetVo = new OrderRetVo();
        BeanUtils.copyProperties(order, orderRetVo);
        String goodsId = order.getGoodsId();
        Optional<Goods> goods = goodsDao.findById(goodsId);
        if (goods.isEmpty()) {
            return orderRetVo;
        }

        String goodsName = goods.map(Goods::getName).orElse(null);
        String coverImgUrl = goods.map(Goods::getCoverImgUrl).orElse(null);
        orderRetVo.setGoods(new OrderRetVo.SimpleGoodsRetVo(goodsId, goodsName, coverImgUrl));
        if (goods.get().getShopId() != null) {
            Admin admin = mongoTemplate.findById(goods.get().getShopId(), Admin.class);
            String shopName = Optional.ofNullable(admin).map(Admin::getShop).map(Shop::getName).orElse(null);
            orderRetVo.setShopName(shopName);
        }
        return orderRetVo;
    }

    @GetMapping("/{ordersId}")
    public Object getOrdersById(@PathVariable String ordersId) {

        Optional<Orders> orders = ordersDao.findById(ordersId);
        if (orders.isEmpty()) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
        }

        Orders order = orders.get();
        OrderRetVo orderRetVo = transformOrderToRetVo(order);
        return Common.decorateReturnObject(new ReturnObject(orderRetVo));
    }



    /**
     * 用户查询自己的订单
     * @param userId
     * @return
     */
    @GetMapping("/user")
    @Audit
    public Object getOrdersByUserId(@LoginUser String userId, @RequestParam(value = "state", required = false) Integer state) {
        Orders orderExample = Orders.builder()
                .userId(userId)
                .state(state)
                .build();

        List<Orders> ordersList = ordersDao.findAll(Example.of(orderExample));
        List<OrderRetVo> orderRetVoList = new ArrayList<>(ordersList.size());
        for (var order : ordersList) {
            OrderRetVo orderRetVo = transformOrderToRetVo(order);
            orderRetVoList.add(orderRetVo);
        }
        return Common.decorateReturnObject(new ReturnObject(orderRetVoList));
    }

    /**
     * 查询店铺订单
     * @param
     * @return
     */
    @GetMapping("")
    public Object getOrdersByShopId(@LoginUser String shopId,
                                    @RequestParam(required = false) Integer state,
                                    @RequestParam(required = false) Integer payWay,
                                    @RequestParam(required = false) String goodsId,
                                    @RequestParam(required = false) String goodsName,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        if (!StringUtils.hasLength(goodsId)) {
            goodsId = null;
        }
        if (!StringUtils.hasLength(goodsName)) {
            goodsName = null;
        }
        Orders orderExample = Orders.builder()
                .shopId(shopId)
                .state(state)
                .payWay(payWay)
                .goodsId(goodsId)
                .goodsName(goodsName)
                .build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("goodsName", ExampleMatcher.GenericPropertyMatcher::contains);
        Page<Orders> ordersPage = ordersDao.findAll(Example.of(orderExample, matcher), PageRequest.of(page, pageSize));
        List<Orders> orders = ordersPage.getContent();

        List<OrderRetVo> orderRetVos = new ArrayList<>(orders.size());
        for (var order : orders) {
            OrderRetVo orderRetVo = transformOrderToRetVo(order);
            orderRetVos.add(orderRetVo);
        }

        return Common.decorateReturnObject(new ReturnObject(new PageImpl<>(orderRetVos, ordersPage.getPageable(), ordersPage.getTotalElements())));
    }

    /**
     * 生成订单
     * @param generateOrderVo
     * @return
     */
    @PostMapping("")
    @Audit
    public Object createOrder(@RequestBody @Valid GenerateOrderVo generateOrderVo, @LoginUser String userId) {
        Goods goods = mongoTemplate.findById(generateOrderVo.getGoodsId(), Goods.class);
        if (goods == null) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "商品不存在"));
        }
        Integer price = goods.getDiscountPrice() != null ? goods.getDiscountPrice() : goods.getPrice();
        Integer payAmount = price * generateOrderVo.getPayNumber();

        Orders orders = new Orders();
        BeanUtils.copyProperties(generateOrderVo, orders);
        orders.setUserId(userId);
        orders.setPayAmount(payAmount);
        orders.setGoodsName(goods.getName());
        orders.setShopId(goods.getShopId());
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
        Update update1 = new Update().set("monthSale",goods.get().getMonthSale()).set("stock",goods.get().getStock()-1);
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
