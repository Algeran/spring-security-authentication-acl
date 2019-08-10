package com.springfreamwork.springsecurity.dao;

import com.springfreamwork.springsecurity.domain.dao.CommentRepository;
import com.springfreamwork.springsecurity.domain.model.Author;
import com.springfreamwork.springsecurity.domain.model.Book;
import com.springfreamwork.springsecurity.domain.model.Comment;
import com.springfreamwork.springsecurity.domain.model.Genre;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.springfreamwork.springsecurity.domain.model.Country.RUSSIA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Test
    public void commentRepositoryShouldInsertEntity() {
        Comment comment = new Comment("user", "so good");
        commentRepository.save(comment);

        Query query = new Query();
        query.addCriteria(
                Criteria.where("username").is(comment.getUsername())
                        .and("comment").is(comment.getComment())
        );
        List<Comment> comments = mongoTemplate.find(query, Comment.class);

        assertThat(comments)
                .as("Checking comment insertion")
                .isNotEmpty()
                .contains(comment);
    }

    @Test
    public void commentRepositoryShouldGetCommentById() {
        Comment comment = new Comment("user", "so good");

        mongoTemplate.save(comment);

        Optional<Comment> commentFromRepo = commentRepository.findById(comment.getId());

        assertThat(commentFromRepo)
                .as("Checking searching book by id")
                .isPresent()
                .get().isEqualTo(comment);
    }

    @Test
    public void commentRepositoryShouldGetCommentByUsername() {
        Comment comment = new Comment("user", "so good");

        mongoTemplate.save(comment);

        List<Comment> comments = commentRepository.findAllByUsername(comment.getUsername());

        assertThat(comments)
                .as("Checking searching comments by username")
                .isNotEmpty()
                .contains(comment);
    }

    @Test
    public void commentRepositoryShouldGetAllComments() {
        Comment comment = new Comment("user", "so good");
        Comment comment_2 = new Comment("user2", "so bad");

        mongoTemplate.save(comment);
        mongoTemplate.save(comment_2);

        List<Comment> comments = commentRepository.findAll();

        assertThat(comments)
                .as("Checking searching all comments")
                .isNotEmpty()
                .contains(comment, comment_2);
    }

    @Test
    public void commentRepositoryShouldDeleteCommentById() {
        Comment comment = new Comment("user", "so good");

        mongoTemplate.save(comment);

        commentRepository.deleteById(comment.getId());

        Optional<Comment> commentFromRepo = commentRepository.findById(comment.getId());

        assertThat(commentFromRepo)
                .as("Checking deleting comment by id")
                .isNotPresent();
    }

    @Test
    public void commentRepositoryShouldDeleteCommentByName() {
        Comment comment = new Comment("user", "so good");

        mongoTemplate.save(comment);

        commentRepository.deleteByUsername(comment.getUsername());

        List<Comment> comments = commentRepository.findAllByUsername(comment.getUsername());

        assertThat(comments)
                .as("Checking deleting comments by username")
                .isEmpty();
    }

    @Test
    public void commentRepositoryShouldReturnCount_2() {
        Comment comment = new Comment("user", "so good");
        Comment comment_2 = new Comment("user2", "so bad");

        long countBefore = commentRepository.count();

        mongoTemplate.save(comment);
        mongoTemplate.save(comment_2);

        long count = commentRepository.count();

        assertThat(count - countBefore)
                .as("Checking counting all comments")
                .isEqualTo(2);
    }

    @Test
    public void commentRepositoryShouldReturnCommentsByBookName() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        Comment comment = new Comment("user", "so good");
        comment.setBooks(Collections.singleton(book));

        mongoTemplate.save(author);
        mongoTemplate.save(genre);
        mongoTemplate.save(book);
        mongoTemplate.save(comment);


        List<Comment> comments = commentRepository.getByBookId(new ObjectId(book.getId()));

        assertThat(comments)
                .as("Checking searching comment by book name")
                .isNotEmpty()
                .contains(comment);
    }
}