package io.github.koh1o.internshiptrackerapi.dto.company;

public record CompanyRequest(
        String name,
        String website,
        String description
) {
}
