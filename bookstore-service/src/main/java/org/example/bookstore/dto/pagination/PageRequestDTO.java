package org.example.bookstore.dto.pagination;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record PageRequestDTO(@PositiveOrZero int page, @Positive int size) {
}
