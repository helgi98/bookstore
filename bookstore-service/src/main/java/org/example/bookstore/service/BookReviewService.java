package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookstore.dto.BookReviewDTO;
import org.example.bookstore.dto.pagination.PageRequestDTO;
import org.example.bookstore.dto.pagination.PageResponseDTO;
import org.example.bookstore.exceptions.ActionForbiddenException;
import org.example.bookstore.exceptions.NotFoundException;
import org.example.bookstore.mongo.document.BookReviewDocument;
import org.example.bookstore.mongo.repo.BookReviewRepo;
import org.example.bookstore.security.SecurityUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookReviewService {

    private final BookService bookService;

    private final BookReviewRepo bookReviewRepo;

    public PageResponseDTO<BookReviewDTO> getBookReviews(Long bookId, PageRequestDTO pageRequest) {
        var book = bookService.getBook(bookId);
        var pagedDocuments = bookReviewRepo.findByBookId(book.id(), PageRequest.of(pageRequest.page(), pageRequest.size()))
            .map(this::mapToDTO);
        return new PageResponseDTO<>(pagedDocuments.getContent(), pagedDocuments.getTotalElements());
    }

    public BookReviewDTO addComment(Long bookId, BookReviewDTO.BookReviewRequest createReviewRequest) {
        log.info("Adding review for book (id: {})", bookId);
        var currentUserId = SecurityUtil.getCurrentUserId();
        var book = bookService.getBook(bookId);
        var reviewDocument = BookReviewDocument.builder()
            .bookId(book.id())
            .userId(currentUserId)
            .review(createReviewRequest.review())
            .rating(createReviewRequest.rating())
            .createdAt(Instant.now())
            .build();

        bookReviewRepo.save(reviewDocument);

        return mapToDTO(reviewDocument);
    }

    public BookReviewDTO updateReview(String reviewId, BookReviewDTO.BookReviewRequest updateReviewRequest) {
        var reviewDocument = getReviewDocument(reviewId);
        validateUserIsOwner(reviewDocument, SecurityUtil.getCurrentUserId());

        log.info("Updating review (id: {})", reviewId);

        reviewDocument.setReview(updateReviewRequest.review());
        reviewDocument.setRating(updateReviewRequest.rating());

        bookReviewRepo.save(reviewDocument);

        return mapToDTO(reviewDocument);
    }

    public void deleteReview(String id) {
        var reviewDocument = getReviewDocument(id);
        validateUserIsOwner(reviewDocument, SecurityUtil.getCurrentUserId());
        log.info("Deleting review (id: {})", id);
        bookReviewRepo.deleteById(id);
    }

    private BookReviewDocument getReviewDocument(String reviewId) {
        return bookReviewRepo.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("Review with id " + reviewId + " not found"));
    }

    private void validateUserIsOwner(BookReviewDocument bookReviewDocument, String userId) {
        if (!Objects.equals(bookReviewDocument.getUserId(), userId)) {
            throw new ActionForbiddenException("Review with id " + bookReviewDocument.getId() + " was posted by other user");
        }
    }

    private BookReviewDTO mapToDTO(BookReviewDocument bookCommentDocument) {
        return new BookReviewDTO(
            bookCommentDocument.getId(),
            bookCommentDocument.getBookId(),
            bookCommentDocument.getUserId(),
            bookCommentDocument.getReview(),
            bookCommentDocument.getRating(),
            bookCommentDocument.getCreatedAt()
        );
    }
}
