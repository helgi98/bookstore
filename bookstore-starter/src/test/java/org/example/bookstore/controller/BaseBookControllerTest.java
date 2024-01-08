package org.example.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookstore.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseBookControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected BookDTO createBook(BookDTO.BookRequest createBookRequest) throws Exception {
        var responseJson = mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createBookRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.title").value(createBookRequest.title()))
            .andExpect(jsonPath("$.author").value(createBookRequest.author()))
            .andExpect(jsonPath("$.isbn").value(createBookRequest.isbn()))
            .andExpect(jsonPath("$.description").value(createBookRequest.description()))
            .andExpect(jsonPath("$.pages").value(createBookRequest.pages()))
            .andExpect(jsonPath("$.price").value(createBookRequest.price()))
            .andExpect(jsonPath("$.publicationYear").value(createBookRequest.publicationYear()))
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseJson, BookDTO.class);
    }

}
