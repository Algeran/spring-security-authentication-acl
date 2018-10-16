package com.springfreamwork.springsecurity.domain.dto;


import com.springfreamwork.springsecurity.domain.model.Comment;

import java.util.Set;

public class CommentCreateDTO {
    private String id;
    private String username;
    private String comment;
    private Set<String> books;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<String> getBooks() {
        return books;
    }

    public void setBooks(Set<String> books) {
        this.books = books;
    }

    public static Comment getComment(CommentCreateDTO commentCreateDTO) {
        return new Comment(commentCreateDTO.getId(),
                commentCreateDTO.getUsername(),
                commentCreateDTO.getComment());
    }

    @Override
    public String toString() {
        return "CommentCreateDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                ", books=" + books +
                '}';
    }
}
