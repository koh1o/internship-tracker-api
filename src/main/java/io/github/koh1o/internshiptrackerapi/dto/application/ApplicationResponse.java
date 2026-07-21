package io.github.koh1o.internshiptrackerapi.dto.application;

import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long vacancyId,
        String vacancyTitle,
        Long companyId,
        String companyName,
        ApplicationStatus status,
        LocalDateTime appliedAt,
        LocalDateTime nextContactAt,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
