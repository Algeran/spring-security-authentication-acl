package com.springfreamwork.springsecurity.domain.com.services;

import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments();

    long countComments();

    Comment getCommentById(String id) throws NotFoundException;

    void updateComment(Comment comment) throws NotFoundException;

    void createComment(Comment comment);

    void deleteComment(String id);
}
