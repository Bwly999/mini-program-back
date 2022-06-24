package cn.edu.xmu.mini.user.service;

import cn.edu.xmu.mini.user.model.User;
import cn.edu.xmu.mini.user.model.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class UserService implements InitializingBean {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void afterPropertiesSet() throws Exception {
        final String collectionName = "user";
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
        }
    }

    public User getUserByOpenId(String openId) {
        Criteria criteria = Criteria.where("openId").is(openId);
        return mongoTemplate.findOne(new Query(criteria), User.class);
    }

    public User saveUser(UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setOpenId(userVo.getOpenId());

        return mongoTemplate.insert(user);
    }

}
