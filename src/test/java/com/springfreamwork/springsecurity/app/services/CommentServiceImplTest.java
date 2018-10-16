package com.springfreamwork.springsecurity.app.services;

import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.CommentService;
import com.springfreamwork.springsecurity.domain.dao.CommentRepository;
import com.springfreamwork.springsecurity.domain.model.Author;
import com.springfreamwork.springsecurity.domain.model.Book;
import com.springfreamwork.springsecurity.domain.model.Comment;
import com.springfreamwork.springsecurity.domain.model.Genre;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.springfreamwork.springsecurity.domain.model.Country.RUSSIA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {

    private CommentService commentService;
    private Author author = new Author("author_id", "author_name", "author_surname", RUSSIA);
    private Genre genre = new Genre("genre_id", "genre_name");
    private Book book = new Book("book_id", "book_name", new Date(), 0, Collections.emptyMap(), Collections.singleton(author), genre);
    private Comment comment = new Comment("comment_id", "username", "comment", Collections.singleton(book));

    @Mock
    private CommentRepository commentRepository;

    @Before
    public void setUp() throws Exception {
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    public void authorServiceShouldReturnAllAuthors() {
        when(commentRepository.findAll()).thenReturn(Collections.singletonList(comment));

        List<Comment> comments = commentService.getAllComments();

        assertThat(comments)
                .as("Checking searching all comments")
                .isNotEmpty()
                .contains(comment);
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void authorServiceShouldReturnCountOfAuthors() {
        when(commentRepository.count()).thenReturn(1L);

        long count = commentService.countComments();

        assertThat(count)
                .as("Checking counting comments")
                .isEqualTo(1L);
        verify(commentRepository, times(1)).count();
    }

    @Test
    public void authorServiceShouldCreateAuthorWithRepo() {
        commentService.createComment(comment);

        verify(commentRepository, times(1)).save(eq(comment));
    }

    @Test
    public void authorServiceShouldReturnAuthorById() {
        when(commentRepository.findById(eq(comment.getId()))).thenReturn(Optional.of(comment));

        try {
            Comment comment = commentService.getCommentById(this.comment.getId());

            assertThat(comment)
                    .as("Check searching comment by id")
                    .isEqualTo(this.comment);
        } catch (NotFoundException e) {
            fail("comment service should not throw ex cause comment not in db");
        }

        verify(commentRepository, times(1)).findById(eq(comment.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void authorServiceShouldThrowExCauseNoSuchAuthorInDb() throws NotFoundException {
        when(commentRepository.findById(anyString())).thenReturn(Optional.empty());

        commentService.getCommentById(comment.getId());
    }

    @Test
    public void authorServiceShouldUpdateAuthor() {
        when(commentRepository.findById(eq(comment.getId()))).thenReturn(Optional.of(comment));

        try {
            commentService.updateComment(comment);
        } catch (NotFoundException e) {
            fail("comment service should not throw ex cause comment in db");
        }

        verify(commentRepository, times(1)).findById(eq(comment.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void authorServiceShouldThrowExCauseNoAuthorToUpdate() throws NotFoundException {
        when(commentRepository.findById(eq(comment.getId()))).thenReturn(Optional.empty());

        commentService.updateComment(comment);
    }

    @Test
    public void authorServiceShouldDeleteAuthorById() {
        commentService.deleteComment(comment.getId());

        verify(commentRepository, times(1)).deleteById(eq(comment.getId()));
    }
}