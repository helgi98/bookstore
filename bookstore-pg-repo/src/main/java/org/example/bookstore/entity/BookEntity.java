package org.example.bookstore.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "book_id_seq"
    )
    @SequenceGenerator(
        name = "book_id_seq",
        sequenceName = "book_id_seq",
        allocationSize = 1
    )
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String isbn;

    @NotNull
    private String description;

    private int pages;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long publicationYear;

}
