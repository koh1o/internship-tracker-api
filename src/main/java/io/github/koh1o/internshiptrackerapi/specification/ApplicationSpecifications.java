package io.github.koh1o.internshiptrackerapi.specification;

import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationFilter;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
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
        return withFilters(
                status,
                vacancyId,
                companyId,
                appliedAtFrom,
                appliedAtTo,
                null
        );
    }

    public static Specification<Application> withFilters(
            ApplicationStatus status,
            Long vacancyId,
            Long companyId,
            LocalDateTime appliedAtFrom,
            LocalDateTime appliedAtTo,
            WorkFormat workFormat
    ) {
        ApplicationFilter filter =
                new ApplicationFilter(
                        status,
                        vacancyId,
                        companyId,
                        appliedAtFrom,
                        appliedAtTo,
                        workFormat
                );

        return withFilters(filter);
    }

    public static Specification<Application> withFilters(
            ApplicationFilter filter
    ) {
        Specification<Application> specification =
                Specification.unrestricted();

        if (filter.status() != null) {
            specification = specification.and(
                    hasStatus(filter.status())
            );
        }

        if (filter.vacancyId() != null) {
            specification = specification.and(
                    hasVacancyId(filter.vacancyId())
            );
        }

        if (filter.companyId() != null) {
            specification = specification.and(
                    hasCompanyId(filter.companyId())
            );
        }

        if (filter.appliedAtFrom() != null) {
            specification = specification.and(
                    hasAppliedAtFrom(filter.appliedAtFrom())
            );
        }

        if (filter.appliedAtTo() != null) {
            specification = specification.and(
                    hasAppliedAtTo(filter.appliedAtTo())
            );
        }

        if (filter.workFormat() != null) {
            specification = specification.and(
                    hasWorkFormat(filter.workFormat())
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

    public static Specification<Application> hasWorkFormat(
            WorkFormat workFormat
    ) {
        return (root, query, criteriaBuilder) -> {
            Path<Vacancy> vacancyPath =
                    root.get("vacancy");

            Path<WorkFormat> workFormatPath =
                    vacancyPath.get("workFormat");

            return criteriaBuilder.equal(
                    workFormatPath,
                    workFormat
            );
        };
    }
}
