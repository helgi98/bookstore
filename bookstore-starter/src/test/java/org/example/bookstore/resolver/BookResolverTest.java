package org.example.bookstore.resolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bookstore.configuration.annotations.SpringIntegrationTest;
import org.example.bookstore.dto.BookDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringIntegrationTest
@Transactional
public class BookResolverTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    private record GraphqlResponse<T>(Map<String, T> data) {
    }

    @Test
    public void test_createAndGetBooks() throws Exception {
        var createdBook = createBook(new BookDTO.BookRequest(
            "The Lord of the Rings",
            "J. R. R. Tolkien",
            "ISBN",
            "Epic fantasy",
            1000,
            BigDecimal.valueOf(19.99),
            1955
        ));
        var query = """
            query bookDetails {
              books(page: 0, size: 1) {
                totalElements
                content {
                  id
                  title
                }
              }
            }
            """;

        var graphqlResponseJson = executeGraphQlQuery(query, "bookDetails");
        var graphqlResponse = objectMapper.readValue(graphqlResponseJson, new TypeReference<GraphqlResponse<PageResponseDTO<BookDTO>>>() {
        });
        PageResponseDTO<BookDTO> books = graphqlResponse.data().get("books");
        assertEquals(1, books.totalElements());
        assertEquals(createdBook.id(), books.content().get(0).id());
        assertEquals("The Lord of the Rings", books.content().get(0).title());
    }

    protected BookDTO createBook(BookDTO.BookRequest createBookRequest) throws Exception {
        var query = """
            mutation updateBookDetails {
              createBook(createBook:{
                title: "%s"
                author: "%s"
                isbn: "%s"
                description: "%s"
                pages: %d
                price: %s
                publicationYear: %d
              }) {
                id
                title
                author
                isbn
                description
                pages
                price
                publicationYear
              }
            }
            """.formatted(
            createBookRequest.title(),
            createBookRequest.author(),
            createBookRequest.isbn(),
            createBookRequest.description(),
            createBookRequest.pages(),
            createBookRequest.price(),
            createBookRequest.publicationYear()
        );


        var graphqlResponseJson = executeGraphQlQuery(query, "updateBookDetails");
        var graphqlResponse = objectMapper.readValue(graphqlResponseJson, new TypeReference<GraphqlResponse<BookDTO>>() {
        });
        return graphqlResponse.data().get("createBook");
    }

    private String executeGraphQlQuery(String query, String operationName) throws Exception {
        var request = Map.of(
            "operationName", operationName,
            "query", query
        );
        var mvcResult = mockMvc.perform(post("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(notNullValue()))
            .andReturn();
        return mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    }
}
