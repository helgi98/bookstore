package org.example.bookstore.mapper;

import lombok.experimental.UtilityClass;
import org.example.bookstore.dto.BookDTO;
import org.example.bookstore.entity.BookEntity;

@UtilityClass
public class BookMapper {

    public BookDTO mapToDTO(BookEntity book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getDescription(),
            book.getPages(), book.getPrice(), book.getPublicationYear());
    }

    public BookEntity mapToEntity(BookDTO.BookRequest book) {
        return mapToEntity(null, book);
    }

    public BookEntity mapToEntity(Long bookId, BookDTO.BookRequest book) {
        return BookEntity.builder()
            .id(bookId)
            .title(book.title())
            .author(book.author())
            .isbn(book.isbn())
            .description(book.description())
            .pages(book.pages())
            .price(book.price())
            .publicationYear(book.publicationYear())
            .build();
    }

}
