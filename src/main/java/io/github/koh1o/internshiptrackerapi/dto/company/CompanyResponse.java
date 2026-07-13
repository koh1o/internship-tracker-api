package io.github.koh1o.internshiptrackerapi.dto.company;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String website,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
