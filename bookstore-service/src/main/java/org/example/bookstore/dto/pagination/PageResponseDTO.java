package org.example.bookstore.dto.pagination;

import java.util.List;

public record PageResponseDTO<T>(List<T> content, long totalElements) {
}
