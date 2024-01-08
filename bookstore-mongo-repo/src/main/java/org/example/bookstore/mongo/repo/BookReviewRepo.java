package org.example.bookstore.mongo.repo;

import org.example.bookstore.mongo.document.BookReviewDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepo extends CrudRepository<BookReviewDocument, String> {

    Page<BookReviewDocument> findByBookId(Long bookId, Pageable pageable);

}
