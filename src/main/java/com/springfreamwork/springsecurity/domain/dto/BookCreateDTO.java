package com.springfreamwork.springsecurity.domain.dto;


import com.springfreamwork.springsecurity.domain.model.Book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class BookCreateDTO {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String id;
    private String name;
    private String date;
    private Set<String> authors;
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

    public void setDate(Date date) {
        this.date = dateFormat.format(date);
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static Book getBook(BookCreateDTO bookDataDTO) throws ParseException {
        Book book = new Book();
        book.setId(bookDataDTO.getId());
        book.setName(bookDataDTO.getName());
        book.setPublishedDate(dateFormat.parse(bookDataDTO.getDate()));
        return book;
    }

    @Override
    public String toString() {
        return "BookDataDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", authors=" + authors +
                ", genre=" + genre +
                '}';
    }
}
