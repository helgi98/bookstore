package org.example.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.bookstore.configuration.annotations.SpringIntegrationTest;
import org.example.bookstore.dto.BookDTO;
import org.example.bookstore.dto.FilterField;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringIntegrationTest
@Transactional
public class BookControllerTest extends BaseBookControllerTest {

    @Test
    public void booksSimpleCrud() throws Exception {
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

        var updateBook = new BookDTO.BookRequest(
            createdBook.title(),
            createdBook.author(),
            createdBook.isbn(),
            createdBook.description(),
            createdBook.pages(),
            BigDecimal.valueOf(26.99),
            createdBook.publicationYear()
        );

        mockMvc.perform(put("/books/" + createdBook.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.title").value(updateBook.title()))
            .andExpect(jsonPath("$.author").value(updateBook.author()))
            .andExpect(jsonPath("$.isbn").value(updateBook.isbn()))
            .andExpect(jsonPath("$.description").value(updateBook.description()))
            .andExpect(jsonPath("$.pages").value(updateBook.pages()))
            .andExpect(jsonPath("$.price").value(updateBook.price()))
            .andExpect(jsonPath("$.publicationYear").value(updateBook.publicationYear()));

        mockMvc.perform(get("/books/" + createdBook.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.title").value(updateBook.title()))
            .andExpect(jsonPath("$.author").value(updateBook.author()))
            .andExpect(jsonPath("$.isbn").value(updateBook.isbn()))
            .andExpect(jsonPath("$.description").value(updateBook.description()))
            .andExpect(jsonPath("$.pages").value(updateBook.pages()))
            .andExpect(jsonPath("$.price").value(updateBook.price()))
            .andExpect(jsonPath("$.publicationYear").value(updateBook.publicationYear()));

        mockMvc.perform(delete("/books/" + createdBook.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBook)))
            .andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @MethodSource("shouldReturnBadRequestWhenBookRequestIsInvalid")
    public void shouldReturnBadRequestWhenCreateBookRequestIsInvalid(BookDTO.BookRequest bookCreateRequest) throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookCreateRequest)))
            .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("shouldReturnBadRequestWhenBookRequestIsInvalid")
    public void shouldReturnBadRequestWhenUpdateBookRequestIsInvalid(BookDTO.BookRequest bookUpdateRequest) throws Exception {
        var bookCreateRequest = new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", "ISBN", "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        );
        var createdBook = createBook(bookCreateRequest);
        mockMvc.perform(put("/books/" + createdBook.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookUpdateRequest)))
            .andExpect(status().isBadRequest());
    }

    private static Stream<BookDTO.BookRequest> shouldReturnBadRequestWhenBookRequestIsInvalid() {
        return Stream.of(new BookDTO.BookRequest(
            null, "J. R. R. Tolkien", "ISBN", "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            " ", "J. R. R. Tolkien", "ISBN", "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", null, "ISBN", "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", " ", "ISBN", "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", null, "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", " ", "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", "ISBN", null, 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", "ISBN", " ", 1000, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", "ISBN", "Epic fantasy", -1, BigDecimal.valueOf(19.99), 1955
        ), new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", "ISBN", "Epic fantasy", 1000, BigDecimal.valueOf(-1), 1955
        ));
    }

    @Test
    @Transactional
    public void shouldFilterBooks() throws Exception {
        var lotrBook = createBook(new BookDTO.BookRequest(
            "The Lord of the Rings", "J. R. R. Tolkien", "ISBN1", "Epic fantasy", 1000, BigDecimal.valueOf(19.99), 1955
        ));
        var hobbitBook = createBook(new BookDTO.BookRequest(
            "The Hobbit", "J. R. R. Tolkien", "ISBN2", "Epic fantasy", 300, BigDecimal.valueOf(10.99), 1937
        ));
        var silmarillionBook = createBook(new BookDTO.BookRequest(
            "The Silmarillion", "J. R. R. Tolkien", "ISBN3", "Epic fantasy", 400, BigDecimal.valueOf(12.99), 1977
        ));
        var hpBook = createBook(new BookDTO.BookRequest(
            "Harry Potter and the Philosopher's Stone", "J. K. Rowling", "ISBN4", "Fantasy", 200, BigDecimal.valueOf(9.99), 1997
        ));

        var bookFilter = new BookDTO.FilterBook(
            new FilterField<>("The Lord of the Rings", FilterField.MatchType.EQ),
            null,
            null,
            null,
            null,
            null,
            null
        );
        var response = filterBooks(0, 10, bookFilter);
        assertEquals(1, response.totalElements());
        assertEquals(1, response.content().size());
        assertEquals(lotrBook.id(), response.content().get(0).id());

        bookFilter = new BookDTO.FilterBook(
            null,
            new FilterField<>("J. R. R. Tolkien", FilterField.MatchType.EQ),
            null,
            null,
            null,
            null,
            null
        );
        response = filterBooks(0, 2, bookFilter);
        assertEquals(3, response.totalElements());
        assertEquals(2, response.content().size());
        assertEquals(lotrBook.id(), response.content().get(0).id());
        assertEquals(hobbitBook.id(), response.content().get(1).id());
        response = filterBooks(1, 2, bookFilter);
        assertEquals(silmarillionBook.id(), response.content().get(0).id());

        bookFilter = new BookDTO.FilterBook(
            null,
            null,
            null,
            null,
            null,
            new FilterField<>(BigDecimal.valueOf(11), FilterField.MatchType.LT),
            null
        );
        response = filterBooks(0, 10, bookFilter);
        assertEquals(2, response.totalElements());
        assertEquals(2, response.content().size());
        assertEquals(hobbitBook.id(), response.content().get(0).id());
        assertEquals(hpBook.id(), response.content().get(1).id());
    }

    private PageResponseDTO<BookDTO> filterBooks(int page, int size, BookDTO.FilterBook filterBook) throws Exception {
        var responseJson = mockMvc.perform(post("/books/filter?page=%d&size=%d".formatted(page, size))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterBook)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseJson, new TypeReference<>() {
        });
    }
}
