package com.springfreamwork.springsecurity.domain.dto;

import com.springfreamwork.springsecurity.domain.model.Genre;

public class GenreDTO {

    private String id;
    private String name;

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

    @Override
    public String toString() {
        return "GenreDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Genre toGenre() {
        return new Genre(id, name);
    }
}
