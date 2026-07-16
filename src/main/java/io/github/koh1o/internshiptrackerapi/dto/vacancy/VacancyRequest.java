package io.github.koh1o.internshiptrackerapi.dto.vacancy;

import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VacancyRequest(

        @NotNull(message = "Company id is required")
        Long companyId,

        @NotBlank(message = "Vacancy title is required")
        @Size(max = 150, message = "Vacancy title must be at most 150 characters")
        String title,

        @Size(max = 500, message = "Link must be at most 500 characters")
        String link,

        @Size(max = 100, message = "City name must be at most 100 characters")
        String city,

        WorkFormat workFormat,

        @Size(max = 2000, message = "Description must be at most 2000 characters")
        String description
) {
}
