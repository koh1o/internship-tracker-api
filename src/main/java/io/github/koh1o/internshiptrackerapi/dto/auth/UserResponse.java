package io.github.koh1o.internshiptrackerapi.dto.auth;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
