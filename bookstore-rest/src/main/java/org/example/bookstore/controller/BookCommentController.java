package org.example.bookstore.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookCommentDTO;
import org.example.bookstore.dto.pagination.PageRequestDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.example.bookstore.service.BookCommentService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookCommentController {

    private final BookCommentService bookCommentService;

    @GetMapping("/{bookId}/comments")
    public PageResponseDTO<BookCommentDTO> getComments(@PathVariable("bookId") @NotNull Long bookId, @ParameterObject @Valid @NotNull PageRequestDTO request) {
        return bookCommentService.getBookComments(bookId, request);
    }

    @PostMapping("/{bookId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public BookCommentDTO addComment(@PathVariable("bookId") @NotNull Long bookId, @RequestBody @Valid @NotNull BookCommentDTO.BookCommentRequest createCommentRequest) {
        return bookCommentService.addComment(bookId, createCommentRequest);
    }

    @PutMapping("/{bookId}/comments/{id}")
    public BookCommentDTO updateComment(@PathVariable("id") @NotNull String id, @RequestBody @Valid @NotNull BookCommentDTO.BookCommentRequest updateBookCommentRequest) {
        return bookCommentService.updateComment(id, updateBookCommentRequest);
    }

    @DeleteMapping("/{bookId}/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") @NotNull String id) {
        bookCommentService.deleteComment(id);
    }

}
