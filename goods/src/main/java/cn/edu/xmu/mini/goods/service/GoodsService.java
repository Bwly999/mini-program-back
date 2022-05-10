package cn.edu.xmu.mini.goods.service;

import cn.edu.xmu.mini.goods.dao.GoodsDao;
import cn.edu.xmu.mini.goods.model.Goods;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GoodsDao goodsDao;

    public Page<Goods> getGoodsUsePage(Integer page, Integer pageSize) {
        return goodsDao.findAll(PageRequest.of(page, pageSize));
    }
}
