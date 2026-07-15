package io.github.koh1o.internshiptrackerapi.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 100, message = "Company name must be at most 100 characters")
        String name,

        @Size(max = 255, message = "Company website must be at most 255 characters")
        String website,

        @Size(max = 1000, message = "Company description must be at most 1000 characters")
        String description
) {
}
