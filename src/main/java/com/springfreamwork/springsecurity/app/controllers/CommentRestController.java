package com.springfreamwork.springsecurity.app.controllers;

import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.BookService;
import com.springfreamwork.springsecurity.domain.com.services.CommentService;
import com.springfreamwork.springsecurity.domain.dto.CommentCreateDTO;
import com.springfreamwork.springsecurity.domain.dto.CommentDTO;
import com.springfreamwork.springsecurity.domain.dto.CommentDataDTO;
import com.springfreamwork.springsecurity.domain.model.Book;
import com.springfreamwork.springsecurity.domain.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommentRestController {

    private final CommentService commentService;
    private final BookService bookService;

    @Autowired
    public CommentRestController(
            CommentService commentService,
            BookService bookService
    ) {
        this.commentService = commentService;
        this.bookService = bookService;
    }

    @GetMapping("/getComments")
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments().stream()
                .map(CommentDTO::getCommentDTO)
                .collect(Collectors.toList());
    }


    @DeleteMapping("/deleteComment")
    @PreAuthorize("hasAuthority('EDIT')")
    public String deleteComment(
            @RequestParam("id") String id
    ) {
        commentService.deleteComment(id);
        return "success";
    }

    @GetMapping("/getComment")
    public CommentDataDTO getCommentData(@RequestParam String id) throws NotFoundException {
        Comment comment = commentService.getCommentById(id);
        List<Book> allBooks = bookService.getAllBooks();

        return CommentDataDTO.getCommentDataDTO(comment, allBooks);
    }

    @GetMapping("/getCommentData")
    public CommentDataDTO createBookPage() {
        List<Book> allBooks = bookService.getAllBooks();
        return CommentDataDTO.getCommentDataDTO(null, allBooks);
    }

    @PostMapping("/editComment")
    @PreAuthorize("hasAuthority('EDIT')")
    public String editComment(@RequestBody CommentCreateDTO commentCreateDTO) throws NotFoundException {
        System.out.println(commentCreateDTO);
        Comment comment = CommentCreateDTO.getComment(commentCreateDTO);
        comment.setBooks(bookService.getBooksById(commentCreateDTO.getBooks()));
        commentService.updateComment(comment);
        return "success";
    }

    @PostMapping("/createComment")
    @PreAuthorize("hasAuthority('EDIT')")
    public String createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        Comment comment = CommentCreateDTO.getComment(commentCreateDTO);
        comment.setBooks(bookService.getBooksById(commentCreateDTO.getBooks()));
        commentService.createComment(comment);
        return "success";
    }
}
