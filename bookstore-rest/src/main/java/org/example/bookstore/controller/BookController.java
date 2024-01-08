package org.example.bookstore.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookDTO;
import org.example.bookstore.dto.pagination.PageRequestDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.example.bookstore.service.BookService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public PageResponseDTO<BookDTO> getBooks(@ParameterObject @NotNull @Valid PageRequestDTO request) {
        return bookService.getBooks(request);
    }

    @PostMapping("filter")
    public PageResponseDTO<BookDTO> filterBooks(@RequestBody BookDTO.FilterBook filterRequest,
                                                @ParameterObject @NotNull @Valid PageRequestDTO request) {
        return bookService.getBooks(filterRequest, request);
    }

    @GetMapping("{id}")
    public BookDTO getBook(@PathVariable("id") @NotNull Long id) {
        return bookService.getBook(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO createBook(@RequestBody @Valid @NotNull BookDTO.BookRequest createRequest) {
        return bookService.createBook(createRequest);
    }

    @PutMapping("{id}")
    public BookDTO updateBook(@PathVariable("id") @NotNull Long id, @RequestBody @Valid @NotNull BookDTO.BookRequest updateRequest) {
        return bookService.updateBook(id, updateRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") @NotNull Long id) {
        bookService.deleteBook(id);
    }

}
