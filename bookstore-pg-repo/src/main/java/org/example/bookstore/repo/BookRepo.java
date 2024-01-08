package org.example.bookstore.repo;

import org.example.bookstore.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookRepo extends JpaRepository<BookEntity, Long>, JpaSpecificationExecutor<BookEntity> {

    Optional<BookEntity> findByIsbn(String isbn);

}
