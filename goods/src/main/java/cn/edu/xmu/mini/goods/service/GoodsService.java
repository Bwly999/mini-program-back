package cn.edu.xmu.mini.goods.service;

import cn.edu.xmu.mini.goods.dao.GoodsDao;
import cn.edu.xmu.mini.goods.model.Goods;
import cn.edu.xmu.mini.goods.model.GoodsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GoodsDao goodsDao;

    public Page<Goods> getGoodsUsePage(Integer page, Integer pageSize) {
        return goodsDao.findAll(PageRequest.of(page, pageSize));
    }

    public Goods saveGoods(GoodsVo goodsVo) {
        Goods goods = new Goods();
        BeanUtils.copyProperties(goodsVo, goods);
        Goods.GoodsDetail goodsDetail = Goods.GoodsDetail.builder()
                                                .originPlace(goodsVo.getOriginPlace())
                                                .level(goodsVo.getLevel())
                                                .weight(goodsVo.getWeight())
                                                .desc(goodsVo.getDesc())
                                                .build();
        goods.setDetail(goodsDetail);

        return goodsDao.insert(goods);
    }
}
