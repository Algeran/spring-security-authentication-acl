package com.springfreamwork.springsecurity.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springfreamwork.springsecurity.domain.com.services.AuthorService;
import com.springfreamwork.springsecurity.domain.com.services.BookService;
import com.springfreamwork.springsecurity.domain.com.services.GenreService;
import com.springfreamwork.springsecurity.domain.dto.BookCreateDTO;
import com.springfreamwork.springsecurity.domain.dto.BookDTO;
import com.springfreamwork.springsecurity.domain.dto.BookDataDTO;
import com.springfreamwork.springsecurity.domain.model.Author;
import com.springfreamwork.springsecurity.domain.model.Book;
import com.springfreamwork.springsecurity.domain.model.Genre;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import static com.springfreamwork.springsecurity.domain.model.Country.RUSSIA;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookRestController.class)
public class BookRestControllerTest {

    private Genre genre = new Genre("genre_id", "genre_name");
    private Author author = new Author("author_id", "author_name", "author_surname", RUSSIA);
    private Book book = new Book("book_id", "book_name", new Date(), 1, Collections.emptyMap(), Collections.singleton(author), genre);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookService bookService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "admin", authorities = "EDIT")
    public void bookControllerShouldDeleteGenreAndRedirectToAllGenresView() throws Exception {
        mockMvc.perform(delete("/deleteBook")
                .param("id", book.getId())
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    @WithMockUser("anon")
    public void bookControllerShouldCallViewWithAllGenresAndFillModel() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Collections.singletonList(book));

        String books = mapper.writeValueAsString(Collections.singleton(BookDTO.getBookDTO(book)));

        mockMvc.perform(get("/getBooks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(books));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    @WithMockUser("anon")
    public void bookControllerShouldReturnBookDTO() throws Exception{
        when(bookService.getBook(eq(book.getId()))).thenReturn(book);
        when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(author));
        when(genreService.getAllGenres()).thenReturn(Collections.singletonList(genre));

        String bookDTO = mapper.writeValueAsString(BookDataDTO.getBookDataDTO(book, Collections.singletonList(author), Collections.singletonList(genre)));

        mockMvc.perform(get("/getBook").param("id", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(bookDTO));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "EDIT")
    public void bookControllerShouldUpdateGenreInDbAndRedirectToAllGenresView() throws Exception {
        BookCreateDTO bookCreateDTO = new BookCreateDTO();
        bookCreateDTO.setId(book.getId());
        bookCreateDTO.setName(book.getName());
        bookCreateDTO.setDate(book.getPublishedDate());
        bookCreateDTO.setAuthors(book.getAuthors().stream().map(Author::getId).collect(Collectors.toSet()));
        bookCreateDTO.setGenre(book.getGenre().getId());

        when(authorService.getAuthorsById(eq(bookCreateDTO.getAuthors()))).thenReturn(Collections.singleton(author));
        when(genreService.getGenreById(eq(bookCreateDTO.getGenre()))).thenReturn(genre);

        String bookCreateDTOString = mapper.writeValueAsString(bookCreateDTO);

        mockMvc.perform(post("/editBook")
                .content(bookCreateDTOString)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .with(csrf())
        ).andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(bookService, times(1)).updateBook(any());
        verify(authorService, times(1)).getAuthorsById(eq(bookCreateDTO.getAuthors()));
        verify(genreService, times(1)).getGenreById(eq(bookCreateDTO.getGenre()));
    }

    @Test
    @WithMockUser("anon")
    public void getBookData() throws Exception{
        when(authorService.getAllAuthors()).thenReturn(Collections.singletonList(author));
        when(genreService.getAllGenres()).thenReturn(Collections.singletonList(genre));

        BookDataDTO bookDataDTO = BookDataDTO.getBookDataDTO(null, Collections.singletonList(author), Collections.singletonList(genre));
        String bookDataDTOString = mapper.writeValueAsString(bookDataDTO);
        mockMvc.perform(get("/getBookData"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(bookDataDTOString));

        verify(authorService, times(1)).getAllAuthors();
        verify(genreService, times(1)).getAllGenres();
    }

    @Test
    @WithMockUser(username = "admin", authorities = "EDIT")
    public void bookControllerShouldCreateGenreOnPostMethodAndRedirectToAllGenresView() throws Exception {
        BookCreateDTO bookCreateDTO = new BookCreateDTO();
        bookCreateDTO.setId(book.getId());
        bookCreateDTO.setName(book.getName());
        bookCreateDTO.setDate(book.getPublishedDate());
        bookCreateDTO.setAuthors(book.getAuthors().stream().map(Author::getId).collect(Collectors.toSet()));
        bookCreateDTO.setGenre(book.getGenre().getId());

        String bookCreateDTOString = mapper.writeValueAsString(bookCreateDTO);

        mockMvc.perform(post("/createBook")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(bookCreateDTOString)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(authorService, times(1)).getAuthorsById(eq(bookCreateDTO.getAuthors()));
        verify(genreService, times(1)).getGenreById(eq(bookCreateDTO.getGenre()));
        verify(bookService, times(1)).createBook(any());
    }
}
