package com.sicredi.desafio.utils;

import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class UtilMethods {
    private static final DecimalFormat formatter = new DecimalFormat("000000");

    public static <D, E> E mapDtoToEntity(D dto, Class<E> entityClass) {
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();

            for (Field fieldDto : dto.getClass().getDeclaredFields()) {
                fieldDto.setAccessible(true);
                Object value = fieldDto.get(dto);

                Field fieldEntity = getFieldByName(entityClass, fieldDto.getName());
                if (fieldEntity != null) {
                    fieldEntity.setAccessible(true);

                    // LocalDate → LocalDateTime
                    if (value instanceof LocalDate && fieldEntity.getType().equals(LocalDateTime.class)) {
                        fieldEntity.set(entity, ((LocalDate) value).atStartOfDay());
                    } else {
                        fieldEntity.set(entity, value);
                    }
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao mapear DTO para entidade", e);
        }
    }

    private static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static <T> String generateNextCode(JpaRepository<T, ?> repository, Function<T, String> getCodigoFunc) {
        Optional<T> ultimo = repository.findAll().stream()
                .filter(e -> getCodigoFunc.apply(e) != null)
                .max((a, b) -> getCodigoFunc.apply(a).compareTo(getCodigoFunc.apply(b)));

        if (ultimo.isEmpty()) {
            return "000001";
        }

        String ultimoCodigo = getCodigoFunc.apply(ultimo.get());
        int numero = Integer.parseInt(ultimoCodigo);
        return String.format("%06d", numero + 1);
    }

}

