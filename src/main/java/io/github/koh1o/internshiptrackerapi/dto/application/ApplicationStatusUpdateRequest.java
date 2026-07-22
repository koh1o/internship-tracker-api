package io.github.koh1o.internshiptrackerapi.dto.application;

import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusUpdateRequest(
        @NotNull(message = "Status is required")
        ApplicationStatus status
) {
}
