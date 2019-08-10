package com.springfreamwork.springsecurity.domain.com.services;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.model.Author;

import java.util.List;
import java.util.Set;

public interface AuthorService {

    List<Author> getAllAuthors();

    long countAuthors();

    void createAuthor(Author author) throws EntityExistsException;

    Author getAuthorById(String authorId) throws NotFoundException;

    Set<Author> getAuthorsById(Set<String> authorsId) throws NotFoundException;

    void updateAuthor(Author author) throws NotFoundException;

    void deleteAuthor(String id);
}
