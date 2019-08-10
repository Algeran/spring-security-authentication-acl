package com.springfreamwork.springsecurity.domain.dto;

import com.springfreamwork.springsecurity.domain.model.Book;
import com.springfreamwork.springsecurity.domain.model.Comment;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CommentDTO {
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

    public static CommentDTO getCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setUsername(comment.getUsername());
        commentDTO.setComment(comment.getComment());
        commentDTO.setBooks(comment.getBooks() != null
                ? comment.getBooks().stream()
                .filter(Objects::nonNull)
                .map(Book::getBookString)
                .collect(Collectors.toSet())
                : Collections.emptySet());
        return commentDTO;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                ", books=" + books +
                '}';
    }
}
