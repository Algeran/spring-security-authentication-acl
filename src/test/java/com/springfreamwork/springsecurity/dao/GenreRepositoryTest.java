package com.springfreamwork.springsecurity.dao;

import com.springfreamwork.springsecurity.domain.dao.GenreRepository;
import com.springfreamwork.springsecurity.domain.model.Genre;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class GenreRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void genreRepositoryShouldInsertEntity() {
        Genre genre = new Genre("fantasy");
        genreRepository.save(genre);

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(genre.getName()));
        List<Genre> genres = mongoTemplate.find(query, Genre.class);

        assertThat(genres)
                .as("Checking genre insertion")
                .isNotEmpty()
                .contains(genre);
    }

    @Test
    public void genreRepositoryShouldGetGenreById() {
        Genre genre = new Genre("fantasy");

        mongoTemplate.save(genre);

        Optional<Genre> genreFromRepository = genreRepository.findById(genre.getId());

        assertThat(genreFromRepository)
                .as("Checking searching genre by id")
                .isPresent()
                .get().isEqualTo(genre);
    }

    @Test
    public void genreRepositoryShouldGetGenreByName() {
        Genre genre = new Genre("fantasy");

        mongoTemplate.save(genre);

        Optional<Genre> genreFromRepository = genreRepository.findByName(genre.getName());

        assertThat(genreFromRepository)
                .as("Checking searching genre by name")
                .isPresent()
                .get().isEqualTo(genre);
    }

    @Test
    public void genreRepositoryShouldGetAllGenres() {
        Genre genre = new Genre("fantasy");
        Genre genre_2 = new Genre("novel");

        mongoTemplate.save(genre);
        mongoTemplate.save(genre_2);

        List<Genre> genres = genreRepository.findAll();

        assertThat(genres)
                .as("Checking searching all genres")
                .isNotEmpty()
                .contains(genre, genre_2);
    }

    @Test
    public void genreRepositoryShouldDeleteGenreById() {
        Genre genre = new Genre("fantasy");

        mongoTemplate.save(genre);
        String id = genre.getId();

        genreRepository.deleteById(id);

        Optional<Genre> genreFromRepository = genreRepository.findById(id);

        assertThat(genreFromRepository)
                .as("Checking deleting genre by id")
                .isNotPresent();
    }

    @Test
    public void genreRepositoryShouldDeleteGenreByName() {
        Genre genre = new Genre("fantasy");

        mongoTemplate.save(genre);

        genreRepository.deleteByName(genre.getName());

        Optional<Genre> genreFromRepository = genreRepository.findByName(genre.getName());

        assertThat(genreFromRepository)
                .as("Checking deleting genre by name")
                .isNotPresent();
    }

    @Test
    public void genreRepositoryShouldReturnCount_2() {
        Genre genre = new Genre("fantasy");
        Genre genre_2 = new Genre("novel");

        long countBefore = genreRepository.count();

        mongoTemplate.save(genre);
        mongoTemplate.save(genre_2);

        long count = genreRepository.count();

        assertThat(count - countBefore)
                .as("Checking counting genres")
                .isEqualTo(2);
    }
}