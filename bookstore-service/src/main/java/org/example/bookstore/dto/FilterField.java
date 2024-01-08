package org.example.bookstore.dto;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public record FilterField<T>(T value, MatchType operator) {

    public enum MatchType {
        LT, LE, GT, GE, EQ, STARTS_WITH, ENDS_WITH, CONTAINS
    }

    public <C> Specification<C> generateCriteria(String fieldName) {
        return (root, query, builder) -> {
            if (value instanceof Number n) {
                Path<Number> field = root.get(fieldName);
                return switch (operator) {
                    case LT -> builder.lt(field, n);
                    case LE -> builder.le(field, n);
                    case GT -> builder.gt(field, n);
                    case GE -> builder.ge(field, n);
                    case EQ -> builder.equal(field, n);
                    default -> null;
                };
            }

            if (value instanceof String s) {
                Path<String> field = root.get(fieldName);
                return switch (operator) {
                    case ENDS_WITH -> builder.like(field, "%" + s);
                    case STARTS_WITH -> builder.like(field, s + "%");
                    case CONTAINS -> builder.like(field, "%" + s + "%");
                    case EQ -> builder.equal(field, s);
                    default -> null;
                };
            }

            return null;
        };
    }

}
