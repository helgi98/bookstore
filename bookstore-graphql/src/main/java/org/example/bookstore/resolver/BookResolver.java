package org.example.bookstore.resolver;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookDTO;
import org.example.bookstore.dto.pagination.PageRequestDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.example.bookstore.service.BookService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BookResolver {

    private final BookService bookService;

    @QueryMapping
    public BookDTO bookById(@Argument("id") long id) {
        return bookService.getBook(id);
    }

    @QueryMapping
    public PageResponseDTO<BookDTO> books(@Argument("page") int page, @Argument("size") int size) {
        return bookService.getBooks(new PageRequestDTO(page, size));
    }

    @QueryMapping
    public PageResponseDTO<BookDTO> filterBooks(@Argument("page") int page, @Argument("size") int size,
                                                @Argument("filter") BookDTO.FilterBook filter) {
        return bookService.getBooks(filter, new PageRequestDTO(page, size));
    }

    @MutationMapping
    public BookDTO createBook(@Argument("createBook") BookDTO.BookRequest createBook) {
        return bookService.createBook(createBook);
    }

    @MutationMapping
    public BookDTO updateBook(@Argument("id") long id, @Argument("updateBook") BookDTO.BookRequest updateBook) {
        return bookService.updateBook(id, updateBook);
    }

    @MutationMapping
    public void deleteBook(@Argument("id") long id) {
        bookService.deleteBook(id);
    }

}
