package org.example.bookstore.mongo.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "book_review")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BookReviewDocument {

    @Id
    private String id;

    private Long bookId;

    private String userId;

    private String review;

    private int rating;

    private Instant createdAt;

}
