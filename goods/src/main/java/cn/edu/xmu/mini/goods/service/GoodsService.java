package cn.edu.xmu.mini.goods.service;

import cn.edu.xmu.mini.goods.dao.GoodsDao;
import cn.edu.xmu.mini.goods.model.CommentVo;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.goods.model.GoodsVo;
import com.google.common.collect.ImmutableList;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class GoodsService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GoodsDao goodsDao;

    public Page<Goods> getGoodsUsePage(Integer page, Integer pageSize) {
        return goodsDao.findAll(PageRequest.of(page, pageSize));
    }


    public Page<Goods> getGoods(String name, String category, Integer priceLeft,Integer priceRight, Integer page, Integer pageSize) {
        Query query = new Query();
        if (name != null) {
            query.addCriteria(Criteria.where("name").regex(String.format("^.*%s.*$", name)));
        }
        if (category != null) {
            query.addCriteria(Criteria.where("category").is(category));
        }
        if (priceLeft != null && priceRight != null && priceLeft <= priceRight) {
            query.addCriteria(Criteria.where("price").gte(priceLeft).lte(priceRight));
        }

        long total = mongoTemplate.count(query, Goods.class);
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        List<Goods> goodsList = mongoTemplate.find(query.with(pageRequest), Goods.class);

        return new PageImpl<>(goodsList, pageRequest, total);
    }

    public List<Goods> getGoodsByName() {
        return goodsDao.findAll();
    }

    public Goods saveGoods(GoodsVo goodsVo) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsVo, goods);
        goods.setState(0);
        return goodsDao.insert(goods);
    }

    public Page<Goods> getGoodsByShopId(String shopId, Integer page, Integer pageSize) {
        Goods goods = Goods.builder()
                .shopId(shopId).build();

        return goodsDao.findAll(Example.of(goods), PageRequest.of(page, pageSize));
    }

    public List<Goods.Comment> getGoodsComment(String goodsId) {
        Optional<Goods> goods = goodsDao.findById(goodsId);
        if (goods.isEmpty()) {
            return List.of();
        }
        return goods.get().getCommentList();
    }

    public void postComment(String goodsId, CommentVo commentVo) {
        Goods.Comment comment = new Goods.Comment();
        BeanUtils.copyProperties(commentVo, comment);
        comment.setCreateTime(LocalDateTime.now());

        Criteria criteria = Criteria.where("id").is(goodsId);
        Update update = new Update().addToSet("commentList", comment);
        mongoTemplate.updateFirst(new Query(criteria), update, Goods.class);

    }

}
