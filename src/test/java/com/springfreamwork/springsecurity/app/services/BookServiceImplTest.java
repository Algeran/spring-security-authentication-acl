package com.springfreamwork.springsecurity.app.services;

import com.springfreamwork.springsecurity.app.utility.EntityExistsException;
import com.springfreamwork.springsecurity.app.utility.NotFoundException;
import com.springfreamwork.springsecurity.domain.com.services.BookService;
import com.springfreamwork.springsecurity.domain.dao.BookRepository;
import com.springfreamwork.springsecurity.domain.model.Author;
import com.springfreamwork.springsecurity.domain.model.Book;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTest {

    private BookService bookService;
    private Author author = new Author("author_id", "author_name", "author_surname", RUSSIA);
    private Genre genre = new Genre("genre_id", "genre_name");
    private Book book = new Book("book_id", "book_name", new Date(), 0, Collections.emptyMap(), Collections.singleton(author), genre);

    @Mock
    private BookRepository bookRepository;

    @Before
    public void setUp() throws Exception {
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    public void authorServiceShouldReturnAllAuthors() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));

        List<Book> books = bookService.getAllBooks();

        assertThat(books)
                .as("Checking searching all books")
                .isNotEmpty()
                .contains(book);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void authorServiceShouldReturnCountOfAuthors() {
        when(bookRepository.count()).thenReturn(1L);

        long count = bookService.countBooks();

        assertThat(count)
                .as("Checking counting books")
                .isEqualTo(1L);
        verify(bookRepository, times(1)).count();
    }

    @Test
    public void authorServiceShouldCreateAuthorWithRepo() {
        when(bookRepository.findByName(anyString())).thenReturn(Optional.empty());

        try {
            bookService.createBook(book);
        } catch (EntityExistsException e) {
            fail("book service should not throw ex cause book already in db");
        }

        verify(bookRepository, times(1)).findByName(eq(book.getName()));
    }

    @Test(expected = EntityExistsException.class)
    public void authorServiceShouldThrowExCauseAuthorAlreadyInDb() throws EntityExistsException {
        when(bookRepository.findByName(anyString())).thenReturn(Optional.of(book));

        bookService.createBook(book);
    }

    @Test
    public void authorServiceShouldReturnAuthorById() {
        when(bookRepository.findById(eq(book.getId()))).thenReturn(Optional.of(book));

        try {
            Book book = bookService.getBook(this.book.getId());

            assertThat(book)
                    .as("Check searching book by id")
                    .isEqualTo(this.book);
        } catch (NotFoundException e) {
            fail("book service should not throw ex cause book not in db");
        }

        verify(bookRepository, times(1)).findById(eq(book.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void authorServiceShouldThrowExCauseNoSuchAuthorInDb() throws NotFoundException {
        when(bookRepository.findById(anyString())).thenReturn(Optional.empty());

        bookService.getBook(book.getId());
    }

    @Test
    public void authorServiceShouldUpdateAuthor() {
        when(bookRepository.findById(eq(book.getId()))).thenReturn(Optional.of(book));

        try {
            bookService.updateBook(book);
        } catch (NotFoundException e) {
            fail("book service should not throw ex cause book in db");
        }

        verify(bookRepository, times(1)).findById(eq(book.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void authorServiceShouldThrowExCauseNoAuthorToUpdate() throws NotFoundException {
        when(bookRepository.findById(eq(book.getId()))).thenReturn(Optional.empty());

        bookService.updateBook(book);
    }

    @Test
    public void authorServiceShouldDeleteAuthorById() {
        bookService.deleteBook(book.getId());

        verify(bookRepository, times(1)).deleteById(eq(book.getId()));
    }
}