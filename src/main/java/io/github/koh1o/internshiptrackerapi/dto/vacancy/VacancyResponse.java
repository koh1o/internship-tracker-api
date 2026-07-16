package io.github.koh1o.internshiptrackerapi.dto.vacancy;

import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;

import java.time.LocalDateTime;

public record VacancyResponse(
        Long id,
        Long companyId,
        String companyName,
        String title,
        String link,
        String city,
        WorkFormat workFormat,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
