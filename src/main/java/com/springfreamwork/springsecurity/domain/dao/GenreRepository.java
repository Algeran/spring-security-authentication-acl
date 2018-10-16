package com.springfreamwork.springsecurity.domain.dao;

import com.springfreamwork.springsecurity.domain.model.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GenreRepository extends MongoRepository<Genre, String> {

    Optional<Genre> findByName(String name);

    void deleteByName(String name);
}
