package io.github.koh1o.internshiptrackerapi.specification;

import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplicationSpecificationsTest {

    @Test
    void shouldCreateStatusSpecification() {
        ApplicationStatus status = ApplicationStatus.INTERVIEW;

        Root<Application> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

        Path<ApplicationStatus> statusPath = mock(Path.class);
        Predicate expectedPredicate = mock(Predicate.class);

        when(root.<ApplicationStatus>get("status"))
                .thenReturn(statusPath);

        when(criteriaBuilder.equal(statusPath, status))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.hasStatus(status);

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);

        verify(root).get("status");
        verify(criteriaBuilder).equal(statusPath, status);
    }

    @Test
    void shouldCreateVacancyIdSpecification() {
        Long vacancyId = 20L;

        Root<Application> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<Vacancy> vacancyPath = mock(Path.class);
        Path<Long> vacancyIdPath = mock(Path.class);
        Predicate expectedPredicate = mock(Predicate.class);

        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);

        when(vacancyPath.<Long>get("id"))
                .thenReturn(vacancyIdPath);

        when(criteriaBuilder.equal(vacancyIdPath, vacancyId))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.hasVacancyId(vacancyId);

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);

        verify(root).get("vacancy");
        verify(vacancyPath).get("id");
        verify(criteriaBuilder).equal(vacancyIdPath, vacancyId);
    }

    @Test
    void shouldCreateUnrestrictedSpecificationWhenFiltersAreMissing() {
        ApplicationStatus status = null;
        Long vacancyId = null;

        Root<Application> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId
                );

        assertNotNull(specification);

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertNull(result);
    }

    @Test
    void shouldCreateSpecificationOnlyForStatus() {
        ApplicationStatus status = ApplicationStatus.INTERVIEW;
        Long vacancyId = null;

        Root<Application> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<ApplicationStatus> statusPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<ApplicationStatus>get("status"))
                .thenReturn(statusPath);
        when(criteriaBuilder.equal(statusPath, status))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);
        verify(root).get("status");
        verify(criteriaBuilder).equal(statusPath, status);
    }

    @Test
    void shouldCreateSpecificationOnlyForVacancyId() {
        ApplicationStatus status = null;
        Long vacancyId = 20L;

        Root<Application> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<Vacancy> vacancyPath =
                mock(Path.class);

        Path<Long> vacancyIdPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);
        when(vacancyPath.<Long>get("id"))
                .thenReturn(vacancyIdPath);
        when(criteriaBuilder.equal(vacancyIdPath, vacancyId))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);
        verify(root).get("vacancy");
        verify(vacancyPath).get("id");
        verify(criteriaBuilder).equal(vacancyIdPath, vacancyId);
    }

    @Test
    void shouldCreateSpecificationForStatusAndVacancyId() {
        ApplicationStatus status = ApplicationStatus.INTERVIEW;
        Long vacancyId = 20L;

        Root<Application> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<ApplicationStatus> statusPath =
                mock(Path.class);

        Path<Vacancy> vacancyPath =
                mock(Path.class);

        Path<Long> vacancyIdPath =
                mock(Path.class);

        Predicate statusPredicate =
                mock(Predicate.class);

        Predicate vacancyPredicate =
                mock(Predicate.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<ApplicationStatus>get("status"))
                .thenReturn(statusPath);
        when(criteriaBuilder.equal(statusPath, status))
                .thenReturn(statusPredicate);
        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);
        when(vacancyPath.<Long>get("id"))
                .thenReturn(vacancyIdPath);
        when(criteriaBuilder.equal(vacancyIdPath, vacancyId))
                .thenReturn(vacancyPredicate);
        when(criteriaBuilder.and(statusPredicate, vacancyPredicate))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);

        verify(root).get("status");
        verify(criteriaBuilder).equal(statusPath, status);
        verify(root).get("vacancy");
        verify(vacancyPath).get("id");
        verify(criteriaBuilder).equal(vacancyIdPath, vacancyId);
        verify(criteriaBuilder).and(statusPredicate, vacancyPredicate);
    }

    @Test
    void shouldCreateCompanyIdSpecification() {
        Long companyId = 5L;

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<Vacancy> vacancyPath =
                mock(Path.class);

        Path<Company> companyPath =
                mock(Path.class);

        Path<Long> companyIdPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);
        when(vacancyPath.<Company>get("company"))
                .thenReturn(companyPath);
        when(companyPath.<Long>get("id"))
                .thenReturn(companyIdPath);
        when(criteriaBuilder.equal(companyIdPath, companyId))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.hasCompanyId(companyId);

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);
        verify(root).get("vacancy");
        verify(vacancyPath).get("company");
        verify(companyPath).get("id");
        verify(criteriaBuilder).equal(companyIdPath, companyId);
    }

    @Test
    void shouldCreateSpecificationOnlyForCompanyId() {
        ApplicationStatus status = null;
        Long vacancyId = null;
        Long companyId = 5L;

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<Vacancy> vacancyPath =
                mock(Path.class);

        Path<Company> companyPath =
                mock(Path.class);

        Path<Long> companyIdPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);
        when(vacancyPath.<Company>get("company"))
                .thenReturn(companyPath);
        when(companyPath.<Long>get("id"))
                .thenReturn(companyIdPath);
        when(criteriaBuilder.equal(companyIdPath, companyId))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId,
                        companyId
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);
        verify(root).get("vacancy");
        verify(vacancyPath).get("company");
        verify(companyPath).get("id");
        verify(criteriaBuilder).equal(companyIdPath, companyId);
    }

    @Test
    void shouldCreateSpecificationForAllFilters() {
        ApplicationStatus status =
                ApplicationStatus.INTERVIEW;

        Long vacancyId = 20L;
        Long companyId = 5L;

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<ApplicationStatus> statusPath =
                mock(Path.class);

        Path<Vacancy> vacancyPath =
                mock(Path.class);

        Path<Long> vacancyIdPath =
                mock(Path.class);

        Path<Company> companyPath =
                mock(Path.class);

        Path<Long> companyIdPath =
                mock(Path.class);

        Predicate statusPredicate =
                mock(Predicate.class);

        Predicate vacancyPredicate =
                mock(Predicate.class);

        Predicate companyPredicate =
                mock(Predicate.class);

        Predicate statusAndVacancyPredicate =
                mock(Predicate.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<ApplicationStatus>get("status"))
                .thenReturn(statusPath);
        when(criteriaBuilder.equal(statusPath, status))
                .thenReturn(statusPredicate);
        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);
        when(vacancyPath.<Long>get("id"))
                .thenReturn(vacancyIdPath);
        when(criteriaBuilder.equal(vacancyIdPath, vacancyId))
                .thenReturn(vacancyPredicate);
        when(vacancyPath.<Company>get("company"))
                .thenReturn(companyPath);
        when(companyPath.<Long>get("id"))
                .thenReturn(companyIdPath);
        when(criteriaBuilder.equal(companyIdPath, companyId))
                .thenReturn(companyPredicate);
        when(criteriaBuilder.and(statusPredicate, vacancyPredicate))
                .thenReturn(statusAndVacancyPredicate);
        when(criteriaBuilder.and(statusAndVacancyPredicate, companyPredicate))
                .thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId,
                        companyId
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);

        verify(root, times(2)).get("vacancy");
        verify(vacancyPath).get("company");
        verify(companyPath).get("id");
        verify(criteriaBuilder).equal(companyIdPath, companyId);
        verify(root).get("status");
        verify(criteriaBuilder).equal(statusPath, status);
        verify(vacancyPath).get("id");
        verify(criteriaBuilder).equal(vacancyIdPath, vacancyId);
        verify(criteriaBuilder).and(statusPredicate, vacancyPredicate);
        verify(criteriaBuilder).and(statusAndVacancyPredicate, companyPredicate);
    }

    @Test
    void shouldCreateAppliedAtFromSpecification() {
        LocalDateTime appliedAtFrom =
                LocalDateTime.of(2026, 7, 1, 0, 0);

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<LocalDateTime> appliedAtPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<LocalDateTime>get("appliedAt"))
                .thenReturn(appliedAtPath);
        when(criteriaBuilder.greaterThanOrEqualTo(
                appliedAtPath,
                appliedAtFrom
        )).thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.hasAppliedAtFrom(
                        appliedAtFrom
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);
        verify(root).get("appliedAt");
        verify(criteriaBuilder).greaterThanOrEqualTo(
                appliedAtPath,
                appliedAtFrom
        );
    }

    @Test
    void shouldCreateAppliedAtToSpecification() {
        LocalDateTime appliedAtTo =
                LocalDateTime.of(2026, 7, 31, 23, 59);

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<LocalDateTime> appliedAtPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<LocalDateTime>get("appliedAt"))
                .thenReturn(appliedAtPath);
        when(criteriaBuilder.lessThanOrEqualTo(
                appliedAtPath,
                appliedAtTo
        )).thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.hasAppliedAtTo(
                        appliedAtTo
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);

        verify(root).get("appliedAt");
        verify(criteriaBuilder).lessThanOrEqualTo(
                appliedAtPath,
                appliedAtTo
        );
    }

    @Test
    void shouldCreateSpecificationForAppliedAtRange() {
        ApplicationStatus status = null;
        Long vacancyId = null;
        Long companyId = null;

        LocalDateTime appliedAtFrom =
                LocalDateTime.of(2026, 7, 1, 0, 0);

        LocalDateTime appliedAtTo =
                LocalDateTime.of(2026, 7, 31, 23, 59);

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<LocalDateTime> appliedAtPath =
                mock(Path.class);

        Predicate fromPredicate =
                mock(Predicate.class);

        Predicate toPredicate =
                mock(Predicate.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<LocalDateTime>get("appliedAt"))
                .thenReturn(appliedAtPath);
        when(criteriaBuilder.greaterThanOrEqualTo(
                appliedAtPath,
                appliedAtFrom
        )).thenReturn(fromPredicate);
        when(criteriaBuilder.lessThanOrEqualTo(
                appliedAtPath,
                appliedAtTo
        )).thenReturn(toPredicate);
        when(criteriaBuilder.and(
                fromPredicate,
                toPredicate
        )).thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId,
                        companyId,
                        appliedAtFrom,
                        appliedAtTo
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);
        verify(root, times(2)).get("appliedAt");
        verify(criteriaBuilder).greaterThanOrEqualTo(
                appliedAtPath,
                appliedAtFrom
        );

        verify(criteriaBuilder).lessThanOrEqualTo(
                appliedAtPath,
                appliedAtTo
        );
        verify(criteriaBuilder).and(
                fromPredicate,
                toPredicate
        );
    }

    @Test
    void shouldCreateWorkFormatSpecification() {
        WorkFormat workFormat =
                WorkFormat.REMOTE;

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<Vacancy> vacancyPath =
                mock(Path.class);

        Path<WorkFormat> workFormatPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);
        when(vacancyPath.<WorkFormat>get("workFormat"))
                .thenReturn(workFormatPath);
        when(criteriaBuilder.equal(
                workFormatPath,
                workFormat
        )).thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.hasWorkFormat(
                        workFormat
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);

        verify(root).get("vacancy");
        verify(vacancyPath).get("workFormat");
        verify(criteriaBuilder).equal(
                workFormatPath,
                workFormat
        );
    }

    @Test
    void shouldCreateSpecificationOnlyForWorkFormat() {
        ApplicationStatus status = null;
        Long vacancyId = null;
        Long companyId = null;
        LocalDateTime appliedAtFrom = null;
        LocalDateTime appliedAtTo = null;

        WorkFormat workFormat =
                WorkFormat.REMOTE;

        Root<Application> root =
                mock(Root.class);

        CriteriaQuery<?> query =
                mock(CriteriaQuery.class);

        CriteriaBuilder criteriaBuilder =
                mock(CriteriaBuilder.class);

        Path<Vacancy> vacancyPath =
                mock(Path.class);

        Path<WorkFormat> workFormatPath =
                mock(Path.class);

        Predicate expectedPredicate =
                mock(Predicate.class);

        when(root.<Vacancy>get("vacancy"))
                .thenReturn(vacancyPath);
        when(vacancyPath.<WorkFormat>get("workFormat"))
                .thenReturn(workFormatPath);
        when(criteriaBuilder.equal(
                workFormatPath,
                workFormat
        )).thenReturn(expectedPredicate);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        status,
                        vacancyId,
                        companyId,
                        appliedAtFrom,
                        appliedAtTo,
                        workFormat
                );

        Predicate result = specification.toPredicate(
                root,
                query,
                criteriaBuilder
        );

        assertSame(expectedPredicate, result);

        verify(root).get("vacancy");
        verify(vacancyPath).get("workFormat");
        verify(criteriaBuilder).equal(
                workFormatPath,
                workFormat
        );
    }
}
