package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.dto.BookCommentDTO;
import org.example.bookstore.dto.pagination.PageRequestDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.example.bookstore.exceptions.ActionForbiddenException;
import org.example.bookstore.exceptions.NotFoundException;
import org.example.bookstore.mongo.document.BookCommentDocument;
import org.example.bookstore.mongo.repo.BookCommentRepo;
import org.example.bookstore.security.SecurityUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCommentService {

    private final BookService bookService;

    private final BookCommentRepo bookCommentRepo;

    public PageResponseDTO<BookCommentDTO> getBookComments(Long bookId, PageRequestDTO pageRequest) {
        var book = bookService.getBook(bookId);
        var pagedDocuments = bookCommentRepo.findByBookId(book.id(), PageRequest.of(pageRequest.page(), pageRequest.size()))
            .map(this::mapToDTO);
        return new PageResponseDTO<>(pagedDocuments.getContent(), pagedDocuments.getTotalElements());
    }

    public BookCommentDTO addComment(Long bookId, BookCommentDTO.BookCommentRequest createCommentRequest) {
        log.info("Adding comment for book (id: {})", bookId);
        var currentUserId = SecurityUtil.getCurrentUserId();
        var book = bookService.getBook(bookId);
        var commentDocument = BookCommentDocument.builder()
            .bookId(book.id())
            .userId(currentUserId)
            .comment(createCommentRequest.comment())
            .createdAt(Instant.now())
            .build();

        bookCommentRepo.save(commentDocument);

        return mapToDTO(commentDocument);
    }

    public BookCommentDTO updateComment(String commentId, BookCommentDTO.BookCommentRequest updateCommentRequest) {
        var commentDocument = getCommentDocument(commentId);
        validateUserIsOwner(commentDocument, SecurityUtil.getCurrentUserId());

        log.info("Updating comment (id: {})", commentId);

        commentDocument.setComment(updateCommentRequest.comment());

        bookCommentRepo.save(commentDocument);

        return mapToDTO(commentDocument);
    }

    public void deleteComment(String id) {
        var commentDocument = getCommentDocument(id);
        validateUserIsOwner(commentDocument, SecurityUtil.getCurrentUserId());
        log.info("Deleting review (id: {})", id);
        bookCommentRepo.deleteById(id);
    }

    private BookCommentDocument getCommentDocument(String commentId) {
        return bookCommentRepo.findById(commentId)
            .orElseThrow(() -> new NotFoundException("Comment with id " + commentId + " not found"));
    }

    private void validateUserIsOwner(BookCommentDocument bookCommentDocument, String userId) {
        if (!Objects.equals(bookCommentDocument.getUserId(), userId)) {
            throw new ActionForbiddenException("Comment with id " + bookCommentDocument.getId() + " was posted by other user");
        }
    }

    private BookCommentDTO mapToDTO(BookCommentDocument bookCommentDocument) {
        return new BookCommentDTO(
            bookCommentDocument.getId(),
            bookCommentDocument.getBookId(),
            bookCommentDocument.getUserId(),
            bookCommentDocument.getComment(),
            bookCommentDocument.getCreatedAt()
        );
    }

}
