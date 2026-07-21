package io.github.koh1o.internshiptrackerapi.dto.application;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ApplicationUpdateRequest(
        @NotNull(message = "Vacancy id is required")
        Long vacancyId,

        LocalDateTime appliedAt,

        LocalDateTime nextContactAt,

        @Size(
                max = 2000,
                message = "Notes must not exceed 2000 characters"
        )
        String notes
) {
}
