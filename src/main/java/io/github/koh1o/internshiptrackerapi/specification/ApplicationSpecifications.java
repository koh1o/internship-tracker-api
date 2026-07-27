package io.github.koh1o.internshiptrackerapi.specification;

import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

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
            Long vacancyId,
            Long companyId
    ) {
        return withFilters(
                status,
                vacancyId,
                companyId,
                null,
                null
        );
    }

    public static Specification<Application> withFilters(
            ApplicationStatus status,
            Long vacancyId
    ) {
        return withFilters(
                status,
                vacancyId,
                null
        );
    }

    public static Specification<Application> withFilters(
            ApplicationStatus status,
            Long vacancyId,
            Long companyId,
            LocalDateTime appliedAtFrom,
            LocalDateTime appliedAtTo
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

        if (companyId != null) {
            specification = specification.and(
                    hasCompanyId(companyId)
            );
        }

        if (appliedAtFrom != null) {
            specification = specification.and(
                    hasAppliedAtFrom(appliedAtFrom)
            );
        }

        if (appliedAtTo != null) {
            specification = specification.and(
                    hasAppliedAtTo(appliedAtTo)
            );
        }

        return specification;
    }

    public static Specification<Application> hasCompanyId(
            Long companyId
    ) {
        return (root, query, criteriaBuilder) -> {
            Path<Vacancy> vacancyPath =
                    root.get("vacancy");

            Path<Company> companyPath =
                    vacancyPath.get("company");

            Path<Long> companyIdPath =
                    companyPath.get("id");

            return criteriaBuilder.equal(
                    companyIdPath, companyId
            );
        };
    }

    public static Specification<Application> hasAppliedAtFrom(
            LocalDateTime appliedAtFrom
    ) {
        return (root, query, criteriaBuilder) -> {
            Path<LocalDateTime> appliedAtPath =
                    root.get("appliedAt");

            return criteriaBuilder.greaterThanOrEqualTo(
                    appliedAtPath,
                    appliedAtFrom
            );
        };
    }

    public static Specification<Application> hasAppliedAtTo(
            LocalDateTime appliedAtTo
    ) {
        return (root, query, criteriaBuilder) -> {
            Path<LocalDateTime> appliedAtPath =
                    root.get("appliedAt");

            return criteriaBuilder.lessThanOrEqualTo(
                    appliedAtPath,
                    appliedAtTo
            );
        };
    }
}
