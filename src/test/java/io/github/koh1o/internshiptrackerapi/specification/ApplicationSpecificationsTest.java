package io.github.koh1o.internshiptrackerapi.specification;

import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
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
}
