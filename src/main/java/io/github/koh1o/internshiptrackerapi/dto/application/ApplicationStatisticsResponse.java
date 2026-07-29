package io.github.koh1o.internshiptrackerapi.dto.application;

import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;

import java.util.Map;

public record ApplicationStatisticsResponse(
        long total,
        Map<ApplicationStatus, Long> byStatus
) {
}
