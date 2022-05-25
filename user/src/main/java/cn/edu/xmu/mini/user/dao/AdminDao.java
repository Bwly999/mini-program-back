package cn.edu.xmu.mini.user.dao;

import cn.edu.xmu.mini.user.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminDao extends MongoRepository<Admin, String> {

}
