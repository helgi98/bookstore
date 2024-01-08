package org.example.bookstore.controller;

import org.example.bookstore.configuration.annotations.SpringIntegrationTest;
import org.example.bookstore.dto.BookCommentDTO;
import org.example.bookstore.dto.BookDTO;
import org.example.bookstore.security.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringIntegrationTest
@Transactional
public class BookCommentControllerTest extends BaseBookControllerTest {

    private static final String USER_ID = "userId";

    private static MockedStatic<SecurityUtil> MOCK;

    @BeforeAll
    public static void init() {
        MOCK = mockStatic(SecurityUtil.class);
        when(SecurityUtil.getCurrentUserId()).thenReturn(USER_ID);
    }

    @AfterAll
    public static void destroy() {
        MOCK.close();
    }

    @Test
    public void commentsSimpleCrud() throws Exception {
        var createBookRequest = new BookDTO.BookRequest(
            "The Lord of the Rings",
            "J. R. R. Tolkien",
            "ISBN",
            "Epic fantasy",
            1000,
            BigDecimal.valueOf(19.99),
            1955
        );

        var createdBook = createBook(createBookRequest);

        var createdComment = createComment(createdBook.id(), new BookCommentDTO.BookCommentRequest("comment1"));

        mockMvc.perform(get("/books/" + createdBook.id() + "/comments?page=0&size=10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(createdComment.id()))
            .andExpect(jsonPath("$.content[0].comment").value(createdComment.comment()));

        var updateComment = new BookCommentDTO.BookCommentRequest("comment2");
        var updatedComment = updateComment(createdBook.id(), createdComment.id(), updateComment);

        mockMvc.perform(get("/books/" + createdBook.id() + "/comments?page=0&size=10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(createdComment.id()))
            .andExpect(jsonPath("$.content[0].comment").value(updatedComment.comment()));
    }

    private BookCommentDTO createComment(Long bookId, BookCommentDTO.BookCommentRequest createCommentRequest) throws Exception {
        var responseJson = mockMvc.perform(post("/books/" + bookId + "/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.bookId").value(bookId))
            .andExpect(jsonPath("$.userId").value(USER_ID))
            .andExpect(jsonPath("$.comment").value(createCommentRequest.comment()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseJson, BookCommentDTO.class);
    }

    private BookCommentDTO updateComment(Long bookId, String commentId, BookCommentDTO.BookCommentRequest updateBookCommentRequest) throws Exception {
        var responseJson = mockMvc.perform(put("/books/%d/comments/%s".formatted(bookId, commentId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBookCommentRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.bookId").value(bookId))
            .andExpect(jsonPath("$.userId").value(USER_ID))
            .andExpect(jsonPath("$.comment").value(updateBookCommentRequest.comment()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseJson, BookCommentDTO.class);
    }

}
