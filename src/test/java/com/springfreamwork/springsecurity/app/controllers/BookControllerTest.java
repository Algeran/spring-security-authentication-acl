package com.springfreamwork.springsecurity.app.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("anon")
    public void bookControllerShouldCallViewWithAllGenresAndFillModel() throws Exception {

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("allBooks"));
    }

    @Test
    @WithMockUser("anon")
    public void bookControllerShouldReturnViewWithNameCreateGenre() throws Exception {

        mockMvc.perform(get("/createBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("createBook"));
    }

    @Test
    @WithMockUser("anon")
    public void bookControllerShouldReturnEditBookPageWithDataInModel() throws Exception {
        mockMvc.perform(get("/editBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"));
    }

}