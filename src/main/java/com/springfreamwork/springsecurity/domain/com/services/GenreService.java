package com.springfreamwork.springsecurity.domain.com.services;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.model.Genre;

import java.util.List;

public interface GenreService {

    Genre createGenre(Genre genre) throws EntityExistsException;

    List<Genre> getAllGenres();

    long countGenres();

    void deleteGenre(String id);

    void updateGenre(Genre genre) throws NotFoundException;

    Genre getGenreById(String genreId) throws NotFoundException;
}
