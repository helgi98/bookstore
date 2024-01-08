package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record BookCommentDTO(String id, Long bookId, String userId, String comment, Instant createdAt) {
    public record BookCommentRequest(@NotBlank String comment) {
    }
}
