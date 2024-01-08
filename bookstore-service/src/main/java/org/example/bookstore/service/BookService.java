package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookDTO;
import org.example.bookstore.dto.pagination.PageRequestDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.example.bookstore.entity.BookEntity;
import org.example.bookstore.exceptions.EntityAlreadyExistsException;
import org.example.bookstore.exceptions.NotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.repo.BookRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;

    @Transactional
    public BookDTO getBook(Long id) {
        var bookEntity = bookRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));

        return BookMapper.mapToDTO(bookEntity);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<BookDTO> getBooks(PageRequestDTO pageRequest) {
        var dbPage = bookRepo.findAll(PageRequest.of(pageRequest.page(), pageRequest.size()))
            .map(BookMapper::mapToDTO);
        return new PageResponseDTO<>(dbPage.getContent(), dbPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<BookDTO> getBooks(BookDTO.FilterBook filterBook, PageRequestDTO pageRequest) {
        Specification<BookEntity> bookFilterSpecification = Specification
            .<BookEntity>where(filterBook.title() != null ? filterBook.title().generateCriteria("title") : null)
            .and(filterBook.author() != null ? filterBook.author().generateCriteria("author") : null)
            .and(filterBook.isbn() != null ? filterBook.isbn().generateCriteria("isbn") : null)
            .and(filterBook.description() != null ? filterBook.description().generateCriteria("description") : null)
            .and(filterBook.pages() != null ? filterBook.pages().generateCriteria("pages") : null)
            .and(filterBook.price() != null ? filterBook.price().generateCriteria("price") : null)
            .and(filterBook.publicationYear() != null ? filterBook.publicationYear().generateCriteria("publicationYear") : null);

        var dbBooks = bookRepo.findAll(bookFilterSpecification, PageRequest.of(pageRequest.page(), pageRequest.size()))
            .map(BookMapper::mapToDTO);

        return new PageResponseDTO<>(dbBooks.getContent(), dbBooks.getTotalElements());
    }

    @Transactional
    public BookDTO createBook(BookDTO.BookRequest createRequest) {
        validateIsbn(null, createRequest.isbn());

        var bookEntity = BookMapper.mapToEntity(createRequest);

        bookRepo.save(bookEntity);

        return BookMapper.mapToDTO(bookEntity);
    }

    @Transactional
    public BookDTO updateBook(long id, BookDTO.BookRequest updateRequest) {
        var bookEntity = bookRepo.findById(id)
            .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
        validateIsbn(id, updateRequest.isbn());

        var updatedBookEntity = BookMapper.mapToEntity(bookEntity.getId(), updateRequest);

        bookRepo.save(updatedBookEntity);

        return BookMapper.mapToDTO(updatedBookEntity);
    }

    @Transactional
    public void deleteBook(long id) {
        bookRepo.deleteById(id);
    }


    private void validateIsbn(Long id, String isbn) {
        bookRepo.findByIsbn(isbn)
            .ifPresent(bookEntity -> {
                if (!bookEntity.getId().equals(id)) {
                    throw new EntityAlreadyExistsException("Book with isbn " + isbn + " already exists");
                }
            });
    }

}
