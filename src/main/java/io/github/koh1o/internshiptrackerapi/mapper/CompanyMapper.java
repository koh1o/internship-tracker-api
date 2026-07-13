package io.github.koh1o.internshiptrackerapi.mapper;

import io.github.koh1o.internshiptrackerapi.dto.company.CompanyRequest;
import io.github.koh1o.internshiptrackerapi.dto.company.CompanyResponse;
import io.github.koh1o.internshiptrackerapi.entity.Company;

public class CompanyMapper {
    public Company toEntity(CompanyRequest request) {
        return new Company(request.name(), request.website(), request.description());
    }

    public CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getWebsite(),
                company.getDescription(),
                company.getCreatedAt(),
                company.getUpdatedAt()
        );
    }
}
