package com.springfreamwork.springsecurity.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {


    @GetMapping("/books")
    public String showAllBooks() {
        return "allBooks";
    }

    @GetMapping("/createBook")
    public String createBookPage() {
        return "createBook";
    }

    @GetMapping("/editBook")
    public String editBookPage(){
        return "editBook";
    }

}
