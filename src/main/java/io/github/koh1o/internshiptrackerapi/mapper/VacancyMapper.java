package io.github.koh1o.internshiptrackerapi.mapper;

import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyRequest;
import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyResponse;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import org.springframework.stereotype.Component;

@Component
public class VacancyMapper {

    public Vacancy toEntity(VacancyRequest request, Company company) {
        return new Vacancy(
                company,
                request.title(),
                request.link(),
                request.city(),
                request.workFormat(),
                request.description()
        );
    }

    public VacancyResponse toResponse(Vacancy vacancy) {
        return new VacancyResponse(
                vacancy.getId(),
                vacancy.getCompany().getId(),
                vacancy.getCompany().getName(),
                vacancy.getTitle(),
                vacancy.getLink(),
                vacancy.getCity(),
                vacancy.getWorkFormat(),
                vacancy.getDescription(),
                vacancy.getCreatedAt(),
                vacancy.getUpdatedAt()
        );
    }
}
