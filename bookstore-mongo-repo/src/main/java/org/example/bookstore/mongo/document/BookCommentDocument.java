package org.example.bookstore.mongo.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "book_comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BookCommentDocument {

    @Id
    private String id;

    private Long bookId;

    private String userId;

    private String comment;

    private Instant createdAt;

}
