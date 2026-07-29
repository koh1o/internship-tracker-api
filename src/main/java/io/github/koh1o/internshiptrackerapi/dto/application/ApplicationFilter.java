package io.github.koh1o.internshiptrackerapi.dto.application;

import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;

import java.time.LocalDateTime;

public record ApplicationFilter(
        ApplicationStatus status,
        Long vacancyId,
        Long companyId,
        LocalDateTime appliedAtFrom,
        LocalDateTime appliedAtTo,
        WorkFormat workFormat,
        LocalDateTime nextContactAtFrom,
        LocalDateTime nextContactAtTo
) {

    public ApplicationFilter(
            ApplicationStatus status,
            Long vacancyId,
            Long companyId,
            LocalDateTime appliedAtFrom,
            LocalDateTime appliedAtTo,
            WorkFormat workFormat
    ) {
        this(
                status,
                vacancyId,
                companyId,
                appliedAtFrom,
                appliedAtTo,
                workFormat,
                null,
                null
        );
    }
}
