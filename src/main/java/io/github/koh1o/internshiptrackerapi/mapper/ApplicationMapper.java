package io.github.koh1o.internshiptrackerapi.mapper;

import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationUpdateRequest;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public Application toEntity(
            ApplicationRequest request,
            Vacancy vacancy
    ) {
        return new Application(
                vacancy,
                request.status(),
                request.appliedAt(),
                request.nextContactAt(),
                request.notes()
        );
    }

    public ApplicationResponse toResponse(Application application) {
        Vacancy vacancy = application.getVacancy();
        Company company = vacancy.getCompany();

        return new ApplicationResponse(
                application.getId(),
                vacancy.getId(),
                vacancy.getTitle(),
                company.getId(),
                company.getName(),
                application.getStatus(),
                application.getAppliedAt(),
                application.getNextContactAt(),
                application.getNotes(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }

    public void updateEntity(
            Application application,
            ApplicationUpdateRequest request,
            Vacancy vacancy
    ) {
        application.setVacancy(vacancy);
        application.setAppliedAt(request.appliedAt());
        application.setNextContactAt(request.nextContactAt());
        application.setNotes(request.notes());
    }
}
