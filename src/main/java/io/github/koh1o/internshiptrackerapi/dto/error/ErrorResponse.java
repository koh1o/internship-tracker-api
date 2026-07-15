package io.github.koh1o.internshiptrackerapi.dto.error;

import java.util.Map;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}

