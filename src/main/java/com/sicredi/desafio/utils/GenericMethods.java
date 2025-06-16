package com.sicredi.desafio.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class GenericMethods {
    public static <E extends Enum<E>> void validateEnumIfPresent(String valor, Class<E> enumClass, RuntimeException ex) {
        if (valor != null && !valor.isBlank()) {
            try {
                Enum.valueOf(enumClass, valor.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw ex;
            }
        }
    }

    public static Pageable validaPageable(Pageable pageable) {
        Pageable pageableNew;
        if (pageable == null || pageable.isUnpaged()) {
            pageableNew = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "codigo"));
        } else if (pageable.getSort().isUnsorted()) {
            pageableNew = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "codigo"));
        } else {
            pageableNew = pageable;
        }
        return pageableNew;
    }
}
