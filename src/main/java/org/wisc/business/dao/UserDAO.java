package org.wisc.business.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.wisc.business.model.UserModel.User;

import java.util.List;

@Repository
public interface UserDAO extends MongoRepository<User, String> {
    User findByEmail(String email);
    User findByUsername(String userName);
    List<User> findAllByNameLike(String name);
}
