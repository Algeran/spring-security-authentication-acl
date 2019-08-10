package com.springfreamwork.springsecurity.app.services;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.AuthorService;
import com.springfreamwork.springsecurity.domain.dao.AuthorRepository;
import com.springfreamwork.springsecurity.domain.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public long countAuthors() {
        return authorRepository.count();
    }

    @Override
    public void createAuthor(Author author) throws EntityExistsException {
        Optional<Author> authorFromRepo = authorRepository.findByNameAndSurname(author.getName(), author.getSurname());
        if (!authorFromRepo.isPresent()) {
            authorRepository.save(author);
        } else {
            throw new EntityExistsException("Автор с таким именем и фамилией уже есть в базе");
        }
    }

    @Override
    public Author getAuthorById(String authorId) throws NotFoundException {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Не найден автор в базе для обновления"));
    }

    @Override
    public Set<Author> getAuthorsById(Set<String> authorsId) throws NotFoundException {
        Set<Author> authors = new HashSet<>();
        authorRepository.findAllById(authorsId).forEach(authors::add);
        return authors;
    }

    @Override
    public void updateAuthor(Author author) throws NotFoundException {
        authorRepository.findById(author.getId())
                .orElseThrow(() -> new NotFoundException("Нет автора в базе для обновления"));
        authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(String id) {
        authorRepository.deleteById(id);
    }
}
