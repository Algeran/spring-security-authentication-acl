package com.springfreamwork.springsecurity.app.controllers;

import com.springfreamwork.springsecurity.domain.com.services.AuthorService;
import com.springfreamwork.springsecurity.domain.com.services.BookService;
import com.springfreamwork.springsecurity.domain.com.services.CommentService;
import com.springfreamwork.springsecurity.domain.com.services.GenreService;
import com.springfreamwork.springsecurity.domain.dto.LibraryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {

    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final CommentService commentService;

    @Autowired
    public LibraryController(
            GenreService genreService,
            AuthorService authorService,
            BookService bookService,
            CommentService commentService
    ) {
        this.genreService = genreService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @GetMapping("/countObjects")
    public LibraryDTO welcomePage() {
        return new LibraryDTO(
                genreService.countGenres(),
                authorService.countAuthors(),
                bookService.countBooks(),
                commentService.countComments()
        );
    }
}
