package cn.edu.xmu.mini.orders.dao;

import cn.edu.xmu.mini.orders.model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersDao extends MongoRepository<Orders, String> {
}
