package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookDTO(Long id, String title, String author, String isbn, String description, int pages,
                      BigDecimal price, Long publicationYear) {

    public record FilterBook(FilterField<String> title, FilterField<String> author, FilterField<String> isbn,
                             FilterField<String> description, FilterField<Integer> pages, FilterField<BigDecimal> price,
                             FilterField<Long> publicationYear) {
    }

    public record BookRequest(@NotBlank String title, @NotBlank String author, @NotBlank String isbn,
                              @NotBlank String description, @Positive Integer pages, @Positive BigDecimal price,
                              long publicationYear) {
    }

}
