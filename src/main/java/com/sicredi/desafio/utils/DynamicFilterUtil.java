package com.sicredi.desafio.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicFilterUtil {

    public static <T, F> Page<T> filter(Class<T> entityClass, F filtroDTO, EntityManager em, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);

        List<Predicate> predicates = buildPredicates(filtroDTO, entityClass, cb, root);

        cq.where(predicates.toArray(new Predicate[0]));

        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (org.springframework.data.domain.Sort.Order sortOrder : pageable.getSort()) {
                Path<Object> path = root.get(sortOrder.getProperty());
                orders.add(sortOrder.isAscending() ? cb.asc(path) : cb.desc(path));
            }
            cq.orderBy(orders);
        }
        var query = em.createQuery(cq);

        if (!pageable.isUnpaged()) {
            query.setFirstResult((int) pageable.getOffset());
        }
        List<T> resultList = query.getResultList();

        // Count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(entityClass);
        List<Predicate> countPredicates = buildPredicates(filtroDTO, entityClass, cb, countRoot);

        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    public static <T, F> List<Predicate> buildPredicates(F filtroDTO, Class<T> entityClass,
                                                         CriteriaBuilder cb, Root<T> root) {
        List<Predicate> predicates = new ArrayList<>();

        // Range dataHoraInclusao
        try {
            Field dataInicialField = filtroDTO.getClass().getDeclaredField("dataInicialInclusao");
            Field dataFinalField = filtroDTO.getClass().getDeclaredField("dataFinalInclusao");
            dataInicialField.setAccessible(true);
            dataFinalField.setAccessible(true);

            Object dataInicialValue = dataInicialField.get(filtroDTO);
            Object dataFinalValue = dataFinalField.get(filtroDTO);

            if (dataInicialValue != null || dataFinalValue != null) {
                Path<LocalDateTime> path = root.get("dataHoraInclusao");
                if (dataInicialValue != null) {
                    LocalDateTime start = (LocalDateTime) dataInicialValue;
                    predicates.add(cb.greaterThanOrEqualTo(path, start));
                }
                if (dataFinalValue != null) {
                    predicates.add(cb.lessThanOrEqualTo(path, (LocalDateTime) dataFinalValue));
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}

        //Aceita lista com mais de um status no filtro
        try {
            Field statusListField = filtroDTO.getClass().getDeclaredField("statusList");
            statusListField.setAccessible(true);
            Object statusListValue = statusListField.get(filtroDTO);

            if (statusListValue != null && statusListValue instanceof List<?>) {
                List<?> statusList = (List<?>) statusListValue;
                if (!statusList.isEmpty()) {
                    Path<String> statusPath = root.get("status");
                    CriteriaBuilder.In<String> inClause = cb.in(statusPath);
                    for (Object status : statusList) {
                        inClause.value(status.toString());
                    }
                    predicates.add(inClause);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}

        // outros campos
        for (Field dtoField : filtroDTO.getClass().getDeclaredFields()) {
            try {
                dtoField.setAccessible(true);
                Object value = dtoField.get(filtroDTO);
                if (value == null) continue;

                Field entityField = getFieldFromEntity(entityClass, dtoField.getName());
                if (entityField == null) continue;

                entityField.setAccessible(true);
                String fieldName = entityField.getName();
                Class<?> entityFieldType = entityField.getType();

                Path<?> path = root.get(fieldName);

                if (value instanceof String) {
                    predicates.add(cb.like(cb.lower(path.as(String.class)), "%" + value.toString().toLowerCase() + "%"));
                } else if (value instanceof BigDecimal) {
                    predicates.add(cb.equal(path.as(BigDecimal.class), value));
                } else if (value instanceof LocalDate && entityFieldType.equals(LocalDateTime.class)) {
                    predicates.add(cb.equal(path.as(LocalDateTime.class), ((LocalDate) value).atStartOfDay()));
                } else if (value instanceof LocalDate) {
                    predicates.add(cb.equal(path.as(LocalDate.class), value));
                } else if (value instanceof LocalDateTime) {
                    predicates.add(cb.equal(path.as(LocalDateTime.class), value));
                } else if (value instanceof Integer) {
                    predicates.add(cb.equal(path.as(Integer.class), value));
                } else if (value instanceof Boolean) {
                    predicates.add(cb.equal(path.as(Boolean.class), value));
                } else {
                    predicates.add(cb.equal(path, value));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return predicates;
    }

    private static <T> Field getFieldFromEntity(Class<T> entityClass, String dtoFieldName) {
        for (Field entityField : entityClass.getDeclaredFields()) {
            if (entityField.getName().equalsIgnoreCase(dtoFieldName)) {
                return entityField;
            }
        }
        return null;
    }
}
