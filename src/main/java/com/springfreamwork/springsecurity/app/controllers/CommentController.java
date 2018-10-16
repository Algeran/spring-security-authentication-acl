package com.springfreamwork.springsecurity.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommentController {

    @GetMapping("/comments")
    public String getAllComments() {
        return "allComments";
    }

    @GetMapping("/createComment")
    public String createCommentPage() {
        return "createComment";
    }

    @GetMapping("/editComment")
    public String editCommentPage() {
        return "editComment";
    }





}
