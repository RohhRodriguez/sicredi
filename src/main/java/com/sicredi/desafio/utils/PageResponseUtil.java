package com.sicredi.desafio.utils;

import org.springframework.data.domain.Page;

public class PageResponseUtil {

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setSize(page.getSize());
        response.setNumberOfElements(page.getNumberOfElements());
        response.setContent(page.getContent());
        return response;
    }
}
