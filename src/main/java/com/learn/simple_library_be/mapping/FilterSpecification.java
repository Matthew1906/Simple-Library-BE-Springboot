package com.learn.simple_library_be.mapping;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class FilterSpecification<T> {

    public Specification<T> build(List<String> filters) {
        if (filters == null || filters.isEmpty()) {
            return Specification.allOf();
        }

        return filters.stream()
            .map(this::parseFilter)
            .reduce(Specification::and)
            .orElse(null);
    }

    private Specification<T> parseFilter(String filter) {
        String[] parts = filter.split("\\+");

        if (parts.length < 2) return null;

        String field = parts[0];
        String type = parts[1];

        return switch (type) {
            case "string" -> stringFilter(field, parts);
            case "select" -> selectFilter(field, parts);
            case "number" -> numberFilter(field, parts);
            case "numrange" -> rangeFilter(field, parts);
            case "date" -> dateFilter(field, parts);
            case "daterange" -> dateFilter(field, parts);
            case "search" -> searchFilter(field, parts);
            default -> null;
        };
    }

    private Specification<T> stringFilter(String field, String[] parts) {
        return (root, query, cb) -> cb.like(root.get(field), "%" + parts[2] + "%");
    }

    @SuppressWarnings("unchecked")
    private Specification<T> selectFilter(String field, String[] parts) {
        return (root, query, cb) -> {
             Path<?> path = root.get(field);
             Class<?> type = path.getJavaType();

            if (type.isEnum()) {
                @SuppressWarnings("rawtypes")
                Class<? extends Enum> enumType = (Class<? extends Enum>) type;

                Object enumValue = Enum.valueOf(enumType, parts[2].toUpperCase());
                return cb.equal(path, enumValue);
            }

            return cb.equal(path, parts[2]);
        };
    }

    private Specification<T> numberFilter(String field, String[] parts) {
        Integer value = Integer.valueOf(parts[2]);
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }

    private Specification<T> rangeFilter(String field, String[] parts) {
        Integer min = Integer.valueOf(parts[2]);
        Integer max = Integer.valueOf(parts[3]);
        return (root, query, cb) -> cb.between(root.get(field), min, max);
    }

    private Specification<T> dateFilter(String field, String[] parts) {
        Instant from = Instant.ofEpochMilli(Long.parseLong(parts[2]));
        Instant to = Instant.ofEpochMilli(Long.parseLong(parts[3]));

        System.out.println(Date.from(from).toString());
        System.out.println(Date.from(to).toString());
        return (root, query, cb) -> cb.between(root.get(field), Date.from(from), Date.from(to));
    }

    private Specification<T> searchFilter(String value, String[] parts) {
        List<String> fields = Arrays.asList(parts).subList(2, parts.length);

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (String field : fields) {
                Path<String> path;

                if (field.contains(".")) {
                    String[] pathParts = field.split("\\.");
                    From<?, ?> join = root;

                    // build join dynamically
                    for (int i = 0; i < pathParts.length - 1; i++) {
                        join = join.join(pathParts[i], JoinType.LEFT);
                    }

                    path = join.get(pathParts[pathParts.length - 1]);
                } else {
                    path = root.get(field);
                }

                predicates.add(cb.like(cb.lower(path), "%" + value.toLowerCase() + "%"));
            }

            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }
}
