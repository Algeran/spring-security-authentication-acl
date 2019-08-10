package com.springfreamwork.springsecurity.app.services;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.BookService;
import com.springfreamwork.springsecurity.domain.dao.BookRepository;
import com.springfreamwork.springsecurity.domain.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository
    ) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public long countBooks() {
        return bookRepository.count();
    }

    @Override
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book getBook(String id) throws NotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не найдено книги в базе"));
    }

    @Override
    public void updateBook(Book book) throws NotFoundException {
        bookRepository.findById(book.getId())
                .orElseThrow(() -> new NotFoundException("Нет книги в базе для обновления"));
        bookRepository.save(book);
    }

    @Override
    public void createBook(Book book) throws EntityExistsException {
        Optional<Book> bookFromRepo = bookRepository.findByName(book.getName());
        if (!bookFromRepo.isPresent()) {
            bookRepository.save(book);
        } else {
            throw new EntityExistsException("Книга с таким наименованием уже в базе");
        }
    }

    @Override
    public Set<Book> getBooksById(Set<String> ids) {
        Set<Book> books = new HashSet<>();
        bookRepository.findAllById(ids).forEach(books::add);
        return books;
    }
}
