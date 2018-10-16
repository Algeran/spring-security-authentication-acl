package com.springfreamwork.springsecurity.domain.dto;


import com.springfreamwork.springsecurity.domain.model.Book;

import java.text.SimpleDateFormat;

public class BookDTO {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private String id;
    private String name;
    private String date;
    private String authors;
    private String genre;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static BookDTO getBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setName(book.getName());
        bookDTO.setDate(dateFormat.format(book.getPublishedDate()));
        bookDTO.setAuthors(book.getAuthorsString());
        bookDTO.setGenre(book.getGenre() == null ? "" : book.getGenre().getName());
        return bookDTO;
    }
}
