package io.github.koh1o.internshiptrackerapi.specification;

import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public final class ApplicationSpecifications {

    private ApplicationSpecifications() {
    }

    public static Specification<Application> hasStatus(
            ApplicationStatus status
    ) {
        return (root, query, criteriaBuilder) -> {
            Path<ApplicationStatus> statusPath =
                    root.get("status");

            return criteriaBuilder.equal(
                    statusPath,
                    status
            );
        };
    }

    public static Specification<Application> hasVacancyId(
            Long vacancyId
    ) {
        return (root, query, criteriaBuilder) -> {
            Path<Vacancy> vacancyPath =
                    root.get("vacancy");

            Path<Long> vacancyIdPath =
                    vacancyPath.get("id");

            return criteriaBuilder.equal(
                    vacancyIdPath,
                    vacancyId
            );
        };
    }

    public static Specification<Application> withFilters(
            ApplicationStatus status,
            Long vacancyId
    ) {
        Specification<Application> specification =
                Specification.unrestricted();

        if (status != null) {
            specification = specification.and(
                    hasStatus(status)
            );
        }

        if (vacancyId != null) {
            specification = specification.and(
                    hasVacancyId(vacancyId)
            );
        }

        return specification;
    }
}
