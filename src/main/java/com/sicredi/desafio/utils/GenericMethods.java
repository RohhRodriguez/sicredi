package com.sicredi.desafio.utils;

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
}
