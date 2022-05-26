package cn.edu.xmu.mini.user.controller;

import cn.edu.xmu.mini.core.aop.Audit;
import cn.edu.xmu.mini.core.aop.LoginUser;
import cn.edu.xmu.mini.core.util.Common;
import cn.edu.xmu.mini.core.util.JwtHelper;
import cn.edu.xmu.mini.core.util.ReturnNo;
import cn.edu.xmu.mini.core.util.ReturnObject;
import cn.edu.xmu.mini.user.dao.AdminDao;
import cn.edu.xmu.mini.user.model.Admin;
import cn.edu.xmu.mini.user.model.Shop;
import cn.edu.xmu.mini.user.model.vo.AdminVo;
import cn.edu.xmu.mini.user.model.vo.LoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/admin", produces = "application/json;charset=UTF-8")
public class AdminController {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AdminDao adminDao;

    private final JwtHelper jwtHelper = new JwtHelper();

    @PostMapping("")
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

    public String creatToken(Admin admin) {
        return jwtHelper.createToken(admin.getId(), admin.getUsername(), 0L, 1, 3 * 24 * 60 * 60);
    }

    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginVo loginVo) {
        Criteria criteria = Criteria.where("username").is(loginVo.getUsername()).and("password").is(loginVo.getPassword());
        Admin admin = mongoTemplate.findOne(new Query(criteria), Admin.class);
        if (admin == null) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.CUSTOMER_INVALID_ACCOUNT));
        }
        String token = creatToken(admin);
        return Common.decorateReturnObject(new ReturnObject(token));
    }

    @GetMapping("/info")
    @Audit
    public Object getAdminInfo(@LoginUser Long userId) {
        Admin admin = mongoTemplate.f(new Query(criteria), Admin.class);
        if (admin == null) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.CUSTOMER_INVALID_ACCOUNT));
        }
        String token = creatToken(admin);
        return Common.decorateReturnObject(new ReturnObject(token));
    }
}
