package com.springfreamwork.springsecurity.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorController {

    @GetMapping("/authors")
    public String showAllAuthors() {
        return "allAuthors";
    }

    @GetMapping("/createAuthor")
    public String createAuthorForm() {
        return "createAuthor";
    }
}
