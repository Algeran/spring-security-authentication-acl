package com.springfreamwork.springsecurity.domain.com.services;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.model.Book;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;
import java.util.Set;

public interface BookService {
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> getAllBooks();

    long countBooks();

    void deleteBook(String id);

    Book getBook(String id) throws NotFoundException;

    void updateBook(Book book) throws NotFoundException;

    void createBook(Book book) throws EntityExistsException;

    Set<Book> getBooksById(Set<String> ids);
}
