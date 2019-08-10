package com.springfreamwork.springsecurity.app.services;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.GenreService;
import com.springfreamwork.springsecurity.domain.dao.GenreRepository;
import com.springfreamwork.springsecurity.domain.model.Genre;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class GenreServiceImplTest {

    private GenreService genreService;
    private Genre genre = new Genre("id", "name");

    @Mock
    private GenreRepository genreRepository;

    @Before
    public void setUp() throws Exception {
        genreService = new GenreServiceImpl(genreRepository);
    }

    @Test
    public void authorServiceShouldReturnAllAuthors() {
        when(genreRepository.findAll()).thenReturn(Collections.singletonList(genre));

        List<Genre> genres = genreService.getAllGenres();

        assertThat(genres)
                .as("Checking searching all genres")
                .isNotEmpty()
                .contains(genre);
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    public void authorServiceShouldReturnCountOfAuthors() {
        when(genreRepository.count()).thenReturn(1L);

        long count = genreService.countGenres();

        assertThat(count)
                .as("Checking counting genres")
                .isEqualTo(1L);
        verify(genreRepository, times(1)).count();
    }

    @Test
    public void authorServiceShouldCreateAuthorWithRepo() {
        when(genreRepository.findByName(anyString())).thenReturn(Optional.empty());

        try {
            genreService.createGenre(genre);
        } catch (EntityExistsException e) {
            fail("genre service should not throw ex cause genre already in db");
        }

        verify(genreRepository, times(1)).findByName(eq(genre.getName()));
    }

    @Test(expected = EntityExistsException.class)
    public void authorServiceShouldThrowExCauseAuthorAlreadyInDb() throws EntityExistsException {
        when(genreRepository.findByName(anyString())).thenReturn(Optional.of(genre));

        genreService.createGenre(genre);
    }

    @Test
    public void authorServiceShouldReturnAuthorById() {
        when(genreRepository.findById(eq(genre.getId()))).thenReturn(Optional.of(genre));

        try {
            Genre genre = genreService.getGenreById(this.genre.getId());

            assertThat(genre)
                    .as("Check searching genre by id")
                    .isEqualTo(this.genre);
        } catch (NotFoundException e) {
            fail("genre service should not throw ex cause genre not in db");
        }

        verify(genreRepository, times(1)).findById(eq(genre.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void authorServiceShouldThrowExCauseNoSuchAuthorInDb() throws NotFoundException {
        when(genreRepository.findById(anyString())).thenReturn(Optional.empty());

        genreService.getGenreById(genre.getId());
    }

    @Test
    public void authorServiceShouldDeleteAuthorById() {
        genreService.deleteGenre(genre.getId());

        verify(genreRepository, times(1)).deleteById(eq(genre.getId()));
    }
}