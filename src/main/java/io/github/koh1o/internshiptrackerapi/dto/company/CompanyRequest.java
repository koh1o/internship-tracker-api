package io.github.koh1o.internshiptrackerapi.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 100, message = "Company name must be at most 100 characters")
        String name,
        String website,
        String description
) {
}
