package com.springfreamwork.springsecurity.dao;

import com.springfreamwork.springsecurity.domain.dao.AuthorRepository;
import com.springfreamwork.springsecurity.domain.model.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.springfreamwork.springsecurity.domain.model.Country.RUSSIA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void authorRepositoryShouldInsertEntity() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        authorRepository.save(author);

        Query query = new Query();
        query.addCriteria(
                Criteria.where("name").is(author.getName())
                        .and("surname").is(author.getSurname()));
        List<Author> authors = mongoTemplate.find(query, Author.class);

        assertThat(authors)
                .as("Checking author insertion")
                .isNotEmpty()
                .contains(author);
    }

    @Test
    public void authorRepositoryShouldGetAuthorById() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        mongoTemplate.save(author);

        Optional<Author> authorFromRepo = authorRepository.findById(author.getId());

        assertThat(authorFromRepo)
                .as("Checking searching author by id")
                .isPresent()
                .get().isEqualTo(author);
    }

    @Test
    public void authorRepositoryShouldGetAuthorByNameAndSurname() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        mongoTemplate.save(author);

        Optional<Author> authorFromRepo = authorRepository.findByNameAndSurname("Leo", "Tolstoy");

        assertThat(authorFromRepo)
                .as("Checking searching author by name and surname")
                .isPresent()
                .get().isEqualTo(author);
    }

    @Test
    public void authorRepositoryShouldGetAllAuthors() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Author author_2 = new Author("Fyodor", "Dostoevsky", RUSSIA);

        mongoTemplate.save(author);
        mongoTemplate.save(author_2);

        List<Author> authors = authorRepository.findAll();

        assertThat(authors)
                .as("Checking searching all authors")
                .isNotEmpty()
                .contains(author, author_2);
    }

    @Test
    public void authorRepositoryShouldDeleteAuthorById() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        mongoTemplate.save(author);

        authorRepository.deleteById(author.getId());

        Optional<Author> authorFromRepo = authorRepository.findById(author.getId());

        assertThat(authorFromRepo)
                .as("Checking deleting author by id")
                .isNotPresent();
    }

    @Test
    public void authorRepositoryShouldDeleteAuthorByNameAndSurname() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        mongoTemplate.save(author);

        authorRepository.deleteByNameAndSurname(author.getName(), author.getSurname());

        Optional<Author> authorFromRepo = authorRepository.findByNameAndSurname("Leo", "Tolstoy");

        assertThat(authorFromRepo)
                .as("Checking deleting author by name and surname")
                .isNotPresent();
    }

    @Test
    public void authorRepositoryShouldReturnCount_2() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Author author_2 = new Author("Fyodor", "Dostoevsky", RUSSIA);

        long countBefore = authorRepository.count();

        mongoTemplate.save(author);
        mongoTemplate.save(author_2);

        long count = authorRepository.count();

        assertThat(count - countBefore)
                .as("Checking counting authors")
                .isEqualTo(2);
    }
}