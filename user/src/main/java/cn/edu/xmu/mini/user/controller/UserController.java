package cn.edu.xmu.mini.user.controller;

import cn.edu.xmu.mini.core.aop.Audit;
import cn.edu.xmu.mini.core.aop.LoginUser;
import cn.edu.xmu.mini.core.exception.InternalServerException;
import cn.edu.xmu.mini.core.util.*;
import cn.edu.xmu.mini.user.config.WeixinConfig;
import cn.edu.xmu.mini.user.exception.UnAuthenticatedException;
import cn.edu.xmu.mini.user.model.Address;
import cn.edu.xmu.mini.user.model.User;
import cn.edu.xmu.mini.user.model.UserVo;
import cn.edu.xmu.mini.user.model.weixin.Session;
import cn.edu.xmu.mini.user.service.UserService;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {
    @Autowired
    private WeixinConfig weixinConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String sessionUrl = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type=authorization_code";

    private RestTemplate restTemplate = new RestTemplate();

    private JwtHelper jwtHelper = new JwtHelper();

    private String getUserOpenId(String code) throws Exception {
        Map<String, String> paramMap = new HashMap<>(4);
        paramMap.put("appid", weixinConfig.getAppId());
        paramMap.put("secret", weixinConfig.getAppSecret());
        paramMap.put("js_code", code);
        String result = restTemplate.getForObject(sessionUrl, String.class, paramMap);
        Session session = JacksonUtil.toObj(result, Session.class);

        if (session == null) {
            throw new InternalServerException();
        }
        else if (session.getErrcode() != null || session.getErrmsg() != null) {
            throw new UnAuthenticatedException(session.getErrmsg());
        }
        return session.getOpenid();
    }



    public String creatToken(User user) {
        return jwtHelper.createToken(user.getId(), user.getNickName(), 0L, 0, 3 * 24 * 60 * 60);
    }

    /**
     * 判断该微信码对应的用户是否存在
     * @return
     * @throws Exception
     */
    @GetMapping("/{code}:exist")
    public Object isUserExist(@PathVariable String code) throws Exception {
        String openId = getUserOpenId(code);
        User user = userService.getUserByOpenId(openId);
        return ResponseUtil.ok(user != null);
    }

    /**
     *
     * @param userVo
     * @return "{\"errno\":0,\"data\":{\"session_key\":\"uQER4NDjMtLx90wnxMuRbg==\",\"openid\":\"o-tMy6JWvFAUp6l7NWHadbr9SlBA\"},\"errmsg\":\"成功\"}"
     */
    @PostMapping("/token")
    public Object login(@RequestBody UserVo userVo) throws Exception {
        String openId = getUserOpenId(userVo.getCode());
        User user = userService.getUserByOpenId(openId);
        // 用户不存在就自动注册
        if (user == null) {
            userVo.setOpenId(openId);
            user = userService.saveUser(userVo);
        }
        String token = creatToken(user);
        return Common.decorateReturnObject(new ReturnObject(token));
    }

    @GetMapping("/info")
    @Audit
    public Object getUserInfo(@LoginUser String userId) {
        User user = mongoTemplate.findById(userId, User.class);
        if (user == null) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
        }
        return Common.decorateReturnObject(new ReturnObject(user));
    }

    /**
     * 增加用户地址
     */
    @PostMapping("/address")
    @Audit
    public Object saveAddress(@RequestBody Address address, @LoginUser String userId) {
        Criteria criteria = Criteria.where("id").is(userId);
        address.setId(ObjectId.get().toString());
        Update update = new Update().addToSet("addressList", address);
        UpdateResult updateResult = mongoTemplate.updateFirst(new Query(criteria), update, User.class);
        if (updateResult.getModifiedCount() == 0) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "地址添加失败"));
        }
        return Common.decorateReturnObject(new ReturnObject());
    }

    /**
     * 获取用户所有地址
     */
    @GetMapping("/address")
    @Audit
    public Object getAddress(@LoginUser String userId) {
        User user = mongoTemplate.findById(userId, User.class);
        if (user == null) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST));
        }
        return ResponseUtil.ok(user.getAddressList());
    }

    /**
     * 修改当前用户底下指定addressId的address
     * @param addressId
     * @param userId
     * @return
     */
    @PutMapping("/address/{id}")
    @Audit
    public Object changeAddressById(@PathVariable("id") String addressId, @LoginUser String userId, @RequestBody Address address) {
        Criteria criteria = Criteria.where("id").is(userId)
                            .and("addressList.id").is(addressId);
        Update update = MongoUtils.getUpdateByObj(address);
        UpdateResult updateResult = mongoTemplate.updateFirst(new Query(criteria), update, User.class);
        if (updateResult.getMatchedCount() == 0) {
            return Common.decorateReturnObject(new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "该地址不存在"));
        }
        return ResponseUtil.ok();
    }
}
