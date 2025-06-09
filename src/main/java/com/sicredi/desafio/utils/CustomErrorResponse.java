package com.sicredi.desafio.utils;

import java.time.Instant;

public record CustomErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String path
) {}
