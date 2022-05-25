package cn.edu.xmu.mini.user.controller;

import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.user.dao.AdminDao;
import cn.edu.xmu.mini.user.model.Admin;
import cn.edu.xmu.mini.user.model.Shop;
import cn.edu.xmu.mini.user.model.vo.AdminVo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/admin", produces = "application/json;charset=UTF-8")
@AllArgsConstructor
public class AdminController {
    private final MongoTemplate mongoTemplate;

    private final AdminDao adminDao;

    @PostMapping("/")
    public Object register(@Valid @RequestBody AdminVo adminvo) {
        Criteria criteria = Criteria.where("username").is(adminvo.getUsername()).and("shop.name").is(adminvo.getShopName());
        Admin existedAdmin = mongoTemplate.findOne(new Query(criteria), Admin.class);
        // 判断是否有信息冲突
        if (existedAdmin != null) {
            String message = "";
            boolean isUsernameConflict = existedAdmin.getUsername() != null && existedAdmin.getUsername().equals(adminvo.getUsername());
            if (isUsernameConflict) {
                message += "用户名已被注册;";
            }
            boolean isShopNameConflict = existedAdmin.getShop().getName() != null && existedAdmin.getShop().getName().equals(adminvo.getShopName());
            if (isShopNameConflict) {
                message += "店铺名已被注册;";
            }
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.CUSTOMER_NAMEEXIST, message));
        }

        Admin admin = new Admin();
        Shop shop = Shop.builder().name(adminvo.getShopName()).build();
        BeanUtils.copyProperties(adminvo, admin);
        admin.setShop(shop);
        mongoTemplate.save(admin);
        return Common.decorateReturnObject(new ReturnObject());
    }
}
