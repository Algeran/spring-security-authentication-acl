package com.springfreamwork.springsecurity.domain.dao;

import com.springfreamwork.springsecurity.domain.model.security.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUsername(String usenname);
}
