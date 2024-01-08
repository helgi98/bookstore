package org.example.bookstore.mongo.repo;

import org.example.bookstore.mongo.document.BookCommentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCommentRepo extends CrudRepository<BookCommentDocument, String> {

    Page<BookCommentDocument> findByBookId(Long bookId, Pageable pageable);

}
