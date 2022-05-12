package cn.edu.xmu.mini.shop.dao;

import cn.edu.xmu.mini.orders.model.Orders;
import cn.edu.xmu.mini.shop.model.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopDao extends MongoRepository<Shop, String> {
}
