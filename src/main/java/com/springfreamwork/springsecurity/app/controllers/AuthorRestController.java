package com.springfreamwork.springsecurity.app.controllers;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.AuthorService;
import com.springfreamwork.springsecurity.domain.dto.AuthorDTO;
import com.springfreamwork.springsecurity.domain.model.Author;
import com.springfreamwork.springsecurity.domain.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class AuthorRestController {

    private final AuthorService authorService;

    @Autowired
    public AuthorRestController(
            AuthorService authorService
    ) {
        this.authorService = authorService;
    }

    @GetMapping("/getAuthors")
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @DeleteMapping("/deleteAuthor")
    @PreAuthorize("hasAuthority('EDIT')")
    public String deleteGenre(@RequestParam("id") String id) {
        authorService.deleteAuthor(id);
        return "success";
    }

    @PostMapping("/editAuthor")
    @PreAuthorize("hasAuthority('EDIT')")
    public String editAuthor(
            @RequestBody AuthorDTO authorDTO
            ) throws NotFoundException {
        Author author = authorService.getAuthorById(authorDTO.getId());
        author.setName(authorDTO.getName());
        author.setSurname(authorDTO.getSurname());
        authorService.updateAuthor(author);
        return "success";
    }


    @GetMapping("/getCountries")
    public List<Country> getAllCountries() {
        return Arrays.asList(Country.values());
    }

    @PostMapping("/createAuthor")
    @PreAuthorize("hasAuthority('EDIT')")
    public String createAuthor(@RequestBody AuthorDTO authorDTO) throws EntityExistsException {
        Author author = authorDTO.toAuthor();
        authorService.createAuthor(author);
        return "success";
    }
}
