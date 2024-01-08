package org.example.bookstore.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookReviewDTO;
import org.example.bookstore.dto.pagination.PageRequestDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.example.bookstore.service.BookReviewService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookReviewController {

    private final BookReviewService bookReviewService;

    @GetMapping("/{bookId}/reviews")
    public PageResponseDTO<BookReviewDTO> getReviews(@PathVariable("bookId") @NotNull Long bookId, @ParameterObject @NotNull @Valid PageRequestDTO request) {
        return bookReviewService.getBookReviews(bookId, request);
    }

    @PostMapping("/{bookId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public BookReviewDTO addReview(@PathVariable("bookId") @NotNull Long bookId, @RequestBody @Valid @NotNull BookReviewDTO.BookReviewRequest bookReviewRequest) {
        return bookReviewService.addComment(bookId, bookReviewRequest);
    }

    @PutMapping("/{bookId}/reviews/{id}")
    public BookReviewDTO updateReview(@PathVariable("id") @NotNull String id, @RequestBody @Valid @NotNull BookReviewDTO.BookReviewRequest updateBookReview) {
        return bookReviewService.updateReview(id, updateBookReview);
    }

    @DeleteMapping("/{bookId}/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable("id") @NotNull String id) {
        bookReviewService.deleteReview(id);
    }

}
