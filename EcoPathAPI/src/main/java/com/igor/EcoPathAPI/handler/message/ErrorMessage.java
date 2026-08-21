package com.igor.EcoPathAPI.handler.message;

import java.time.Instant;

public record ErrorMessage(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}
