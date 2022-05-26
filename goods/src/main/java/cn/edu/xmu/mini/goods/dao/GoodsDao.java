package cn.edu.xmu.mini.goods.dao;

import cn.edu.xmu.mini.goods.model.Goods;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GoodsDao extends MongoRepository<Goods, String>{
}
