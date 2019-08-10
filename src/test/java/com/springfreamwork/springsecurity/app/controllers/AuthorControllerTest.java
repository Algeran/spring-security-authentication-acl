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
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser("anon")
    public void authorControllerShouldCallViewWithAllGenresAndFillModel() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(view().name("allAuthors"));
    }

    @Test
    @WithMockUser("anon")
    public void authorControllerShouldReturnViewWithNameCreateGenre() throws Exception {
        mockMvc.perform(get("/createAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("createAuthor"));
    }
}