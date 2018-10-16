package com.springfreamwork.springsecurity.app.configurations;

import com.springfreamwork.springsecurity.domain.dao.AuthorRepository;
import com.springfreamwork.springsecurity.domain.dao.BookRepository;
import com.springfreamwork.springsecurity.domain.dao.GenreRepository;
import com.springfreamwork.springsecurity.domain.dao.UserRepository;
import com.springfreamwork.springsecurity.domain.model.Author;
import com.springfreamwork.springsecurity.domain.model.Book;
import com.springfreamwork.springsecurity.domain.model.Country;
import com.springfreamwork.springsecurity.domain.model.Genre;
import com.springfreamwork.springsecurity.domain.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;

@Service
public class DataCreatorService {

    private final UserRepository userRepository;
    private final DataSource dataSource;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public DataCreatorService(
            UserRepository userRepository,
            DataSource dataSource,
            GenreRepository genreRepository,
            AuthorRepository authorRepository,
            BookRepository bookRepository
    ) {
        this.userRepository = userRepository;
        this.dataSource = dataSource;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void createUsersAndACLs() throws SQLException {
        userRepository.deleteAll();

        bookRepository.deleteAll();
        genreRepository.deleteAll();
        authorRepository.deleteAll();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);
        String password = encoder.encode("password");
        User user = new User("admin", password, "READ,READ_ADVANCED,EDIT");
        User admin = userRepository.save(user);

        user = new User("user", password, "READ");
        User regular = userRepository.save(user);

        user = new User("advanced_user", password, "READ,READ_ADVANCED");
        User advanced = userRepository.save(user);

        genreRepository.deleteByName("ACL_GENRE");
        Genre genre = new Genre("ACL_GENRE");
        genre  = genreRepository.save(genre);

        authorRepository.deleteByNameAndSurname("ACL_NAME", "ACL_SURNAME");
        Author author = new Author("ACL_NAME", "ACL_SURNAME", Country.RUSSIA);
        author = authorRepository.save(author);

        bookRepository.deleteByName("ACL_BOOK_TO_ALL");
        Book bookToAll = new Book("ACL_BOOK_TO_ALL", new Date(), Collections.emptyMap(), Collections.singleton(author), genre);
        bookToAll = bookRepository.save(bookToAll);
        Book bookToAdvanced = new Book("ACL_BOOK_TO_ADVANCED", new Date(), Collections.emptyMap(), Collections.singleton(author), genre);
        bookToAdvanced = bookRepository.save(bookToAdvanced);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO acl_class (id, class, class_id_type) VALUES (1, 'com.springfreamwork.springsecurity.domain.model.Book', 'java.lang.String')");

        statement.executeUpdate("INSERT INTO acl_sid (id, principal, sid) VALUES" +
                "  (1, 1, 'admin')," +
                "  (2, 1, 'user')," +
                "  (3, 1, 'advanced_user')");

        statement.executeUpdate("INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES" +
                "  (1, 1, '" + bookToAll.getId() + "', NULL, 1, 0)," +
                "  (2, 1, '" + bookToAdvanced.getId() + "', NULL, 1, 0)");

        statement.executeUpdate("INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES" +
                "  (1, 1, 1, 1, 1, 1, 1, 1)," +
                "  (2, 1, 2, 2, 1, 1, 1, 1)," +
                "  (3, 1, 3, 3, 1, 1, 1, 1)," +
                "  (4, 2, 1, 1, 1, 1, 1, 1)," +
                "  (5, 2, 2, 2, 1, 0, 1, 1)," +
                "  (6, 2, 3, 3, 1, 1, 1, 1)");
        statement.close();
        connection.close();
    }
}
