package com.springfreamwork.springsecurity.domain.dao;

import com.springfreamwork.springsecurity.domain.model.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByName(String name);

    void deleteByName(String name);

    @Query("{'authors.$id' : ?0}")
    List<Book> getByAuthorId(ObjectId authorId);

    @Query("{'genre.$id' : ?0}")
    List<Book> getByGenreId(ObjectId genreId);
}
