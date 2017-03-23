package com.nick.repository.mongo;

import com.nick.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by qmaolong on 2017/3/23.
 */
public interface UserDao extends MongoRepository<User, String> {

    User findByName(String name);
    List<User> findById(String id);

}