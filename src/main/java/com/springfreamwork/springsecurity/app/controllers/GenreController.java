package com.springfreamwork.springsecurity.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenreController {

    @GetMapping("/genres")
    public String showAllGenres() {
        return "allGenres";
    }

    @GetMapping("/createGenre")
    public String createGenreForm() {
        return "createGenre";
    }

}
