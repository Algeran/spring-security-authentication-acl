package com.springfreamwork.springsecurity.app.controllers;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.GenreService;
import com.springfreamwork.springsecurity.domain.dto.GenreDTO;
import com.springfreamwork.springsecurity.domain.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GenreRestController {

    private final GenreService genreService;

    @Autowired
    public GenreRestController(
            GenreService genreService
    ) {
        this.genreService = genreService;
    }

    @DeleteMapping("/deleteGenre")
    @PreAuthorize("hasAuthority('EDIT')")
    public String deleteGenre(@RequestParam("id") String id) {
        genreService.deleteGenre(id);
        return "success";
    }

    @GetMapping("/getGenres")
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @PostMapping("/editGenre")
    @PreAuthorize("hasAuthority('EDIT')")
    public String editGenre(
            @RequestBody GenreDTO genreDTO) throws NotFoundException {
        Genre genre = genreService.getGenreById(genreDTO.getId());
        genre.setName(genreDTO.getName());
        genreService.updateGenre(genre);
        return "success";
    }

    @PostMapping("/createGenre")
    @PreAuthorize("hasAuthority('EDIT')")
    public String createGenre(
            @RequestBody GenreDTO genreDTO) throws EntityExistsException {
        Genre genre = genreDTO.toGenre();
        genreService.createGenre(genre);
        return "success";
    }

}
