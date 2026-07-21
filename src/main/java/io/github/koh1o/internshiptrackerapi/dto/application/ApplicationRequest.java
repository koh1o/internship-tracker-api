package io.github.koh1o.internshiptrackerapi.dto.application;

import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ApplicationRequest(

        @NotNull(message = "Vacancy id is required")
        Long vacancyId,

        @NotNull(message = "Status is required")
        ApplicationStatus status,

        LocalDateTime appliedAt,

        LocalDateTime nextContactAt,

        @Size(
                max = 2000,
                message = "Notes must not exceed 2000 characters"
        )
        String notes
) {
}
