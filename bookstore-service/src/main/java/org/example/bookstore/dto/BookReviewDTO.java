package org.example.bookstore.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record BookReviewDTO(String id, Long bookId, String userId, String review, int rating, Instant createdAt) {
    public record BookReviewRequest(@NotBlank String review, @Min(1) @Max(5) int rating) {
    }
}
