package cn.edu.xmu.mini.shop.dao;

import cn.edu.xmu.mini.shop.model.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopDao extends MongoRepository<Shop, String> {
}
