package io.github.koh1o.internshiptrackerapi.repository;

import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationFilter;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import io.github.koh1o.internshiptrackerapi.specification.ApplicationSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldSaveApplication() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 7, 21, 10, 0);

        LocalDateTime nextContactAt =
                LocalDateTime.of(2026, 7, 28, 10, 0);

        Company savedCompany = saveCompany(
                "Example Company",
                "https://example.com",
                "Test company"
        );

        Vacancy savedVacancy = saveVacancy(
                savedCompany,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description"
        );

        Application application = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                nextContactAt,
                "Waiting for response"
        );

        Application savedApplication =
                applicationRepository.saveAndFlush(application);

        assertNotNull(savedApplication.getId());
        assertEquals(savedVacancy, savedApplication.getVacancy());
        assertEquals(ApplicationStatus.APPLIED, savedApplication.getStatus());
        assertEquals(appliedAt, savedApplication.getAppliedAt());
        assertEquals(nextContactAt, savedApplication.getNextContactAt());
        assertEquals("Waiting for response", savedApplication.getNotes());
        assertNotNull(savedApplication.getCreatedAt());
        assertNotNull(savedApplication.getUpdatedAt());
    }

    @Test
    void shouldFilterApplicationsByStatus() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 1, 10, 0);

        Company savedCompany = saveCompany(
                "Specification Test Company",
                "https://specification.example.com",
                "Company for specification integration test"
        );

        Vacancy savedVacancy = saveVacancy(
                savedCompany,
                "Java Backend Intern",
                "https://specification.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for specification integration test"
        );

        Application interviewApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Interview application"
        );

        saveApplication(
                savedVacancy,
                ApplicationStatus.REJECTED,
                appliedAt,
                null,
                "Rejected application"
        );

        Specification<Application> specification =
                ApplicationSpecifications.hasStatus(
                        ApplicationStatus.INTERVIEW
                );

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(
                ApplicationStatus.INTERVIEW,
                applications.getFirst().getStatus()
        );
        assertEquals(
                interviewApplication.getId(),
                applications.getFirst().getId()
        );
    }

    @Test
    void shouldFilterApplicationsByCompanyId() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 1, 10, 0);

        Company savedFirstCompany = saveCompany(
                "First Company",
                "https://first.example.com",
                "First company"
        );

        Company savedSecondCompany = saveCompany(
                "Second Company",
                "https://second.example.com",
                "Second company"
        );

        Vacancy savedFirstVacancy = saveVacancy(
                savedFirstCompany,
                "First Java Intern",
                "https://first.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "First vacancy"
        );

        Vacancy savedSecondVacancy = saveVacancy(
                savedSecondCompany,
                "Second Java Intern",
                "https://second.example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Second vacancy"
        );

        Application firstApplication = saveApplication(
                savedFirstVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "First application"
        );

        saveApplication(
                savedSecondVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Second application"
        );

        Specification<Application> specification =
                ApplicationSpecifications.hasCompanyId(
                        savedFirstCompany.getId()
                );

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(
                firstApplication.getId(),
                applications.getFirst().getId()
        );
        assertEquals(
                savedFirstCompany.getId(),
                applications.getFirst().getVacancy().getCompany().getId()
        );
    }

    @Test
    void shouldFilterApplicationsByWorkFormat() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 2, 10, 0);

        Company savedCompany = saveCompany(
                "Work Format Company",
                "https://work-format.example.com",
                "Company for work format filtering"
        );

        Vacancy savedRemoteVacancy = saveVacancy(
                savedCompany,
                "Remote Java Intern",
                "https://work-format.example.com/remote",
                null,
                WorkFormat.REMOTE,
                "Remote vacancy"
        );

        Vacancy savedOfficeVacancy = saveVacancy(
                savedCompany,
                "Office Java Intern",
                "https://work-format.example.com/office",
                "Oslo",
                WorkFormat.OFFICE,
                "Office vacancy"
        );

        Application remoteApplication = saveApplication(
                savedRemoteVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Remote application"
        );

        saveApplication(
                savedOfficeVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Office application"
        );

        Specification<Application> specification =
                ApplicationSpecifications.hasWorkFormat(
                        WorkFormat.REMOTE
                );

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(
                remoteApplication.getId(),
                applications.getFirst().getId()
        );
        assertEquals(
                WorkFormat.REMOTE,
                applications.getFirst().getVacancy().getWorkFormat()
        );
    }

    @Test
    void shouldFilterApplicationsByAppliedAtRange() {
        LocalDateTime appliedAtFrom =
                LocalDateTime.of(2026, 8, 10, 0, 0);

        LocalDateTime appliedAtTo =
                LocalDateTime.of(2026, 8, 20, 23, 59);

        LocalDateTime beforeRange =
                LocalDateTime.of(2026, 8, 5, 10, 0);

        LocalDateTime insideRange =
                LocalDateTime.of(2026, 8, 15, 10, 0);

        LocalDateTime afterRange =
                LocalDateTime.of(2026, 8, 25, 10, 0);

        Company savedCompany = saveCompany(
                "Date Filter Company",
                "https://date-filter.example.com",
                "Company for date filtering"
        );

        Vacancy savedVacancy = saveVacancy(
                savedCompany,
                "Java Intern",
                "https://date-filter.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for date filtering"
        );

        saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                beforeRange,
                null,
                "Before range"
        );

        Application insideApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                insideRange,
                null,
                "Inside range"
        );

        saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                afterRange,
                null,
                "After range"
        );

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        null,
                        null,
                        null,
                        appliedAtFrom,
                        appliedAtTo
                );

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(
                insideApplication.getId(),
                applications.getFirst().getId()
        );
        assertEquals(
                insideRange,
                applications.getFirst().getAppliedAt()
        );
    }

    @Test
    void shouldFilterApplicationsByVacancyId() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 3, 10, 0);

        Company savedCompany = saveCompany(
                "Vacancy Filter Company",
                "https://vacancy-filter.example.com",
                "Company for vacancy filtering"
        );

        Vacancy savedFirstVacancy = saveVacancy(
                savedCompany,
                "First Java Intern",
                "https://vacancy-filter.example.com/first",
                "Oslo",
                WorkFormat.HYBRID,
                "First vacancy"
        );

        Vacancy savedSecondVacancy = saveVacancy(
                savedCompany,
                "Second Java Intern",
                "https://vacancy-filter.example.com/second",
                "Oslo",
                WorkFormat.HYBRID,
                "Second vacancy"
        );

        Application firstApplication = saveApplication(
                savedFirstVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "First application"
        );

        saveApplication(
                savedSecondVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Second application"
        );

        Specification<Application> specification =
                ApplicationSpecifications.hasVacancyId(
                        savedFirstVacancy.getId()
                );

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(
                firstApplication.getId(),
                applications.getFirst().getId()
        );
        assertEquals(
                savedFirstVacancy.getId(),
                applications.getFirst().getVacancy().getId()
        );
    }

    @Test
    void shouldFilterApplicationsByNextContactAtRange() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 1, 10, 0);

        LocalDateTime nextContactAtFrom =
                LocalDateTime.of(2026, 8, 10, 0, 0);

        LocalDateTime nextContactAtTo =
                LocalDateTime.of(2026, 8, 20, 23, 59);

        LocalDateTime beforeRange =
                LocalDateTime.of(2026, 8, 5, 10, 0);

        LocalDateTime insideRange =
                LocalDateTime.of(2026, 8, 15, 10, 0);

        LocalDateTime afterRange =
                LocalDateTime.of(2026, 8, 25, 10, 0);

        Company savedCompany = saveCompany(
                "Next Contact Company",
                "https://next-contact.example.com",
                "Company for next contact filtering"
        );

        Vacancy savedVacancy = saveVacancy(
                savedCompany,
                "Java Backend Intern",
                "https://next-contact.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for next contact filtering"
        );

        saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                beforeRange,
                "Before next contact range"
        );

        Application insideApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                insideRange,
                "Inside next contact range"
        );

        saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                afterRange,
                "After next contact range"
        );

        ApplicationFilter filter =
                new ApplicationFilter(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        nextContactAtFrom,
                        nextContactAtTo
                );

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(filter);

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(
                insideApplication.getId(),
                applications.getFirst().getId()
        );
        assertEquals(
                insideRange,
                applications.getFirst().getNextContactAt()
        );
    }

    @Test
    void shouldFilterApplicationsByMultipleFilters() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 5, 10, 0);

        Company savedFirstCompany = saveCompany(
                "Combined Filter Company",
                "https://combined.example.com",
                "Matching company"
        );

        Company savedSecondCompany = saveCompany(
                "Other Company",
                "https://other.example.com",
                "Non-matching company"
        );

        Vacancy savedMatchingVacancy = saveVacancy(
                savedFirstCompany,
                "Remote Java Intern",
                "https://combined.example.com/vacancy",
                null,
                WorkFormat.REMOTE,
                "Matching vacancy"
        );

        Vacancy savedOtherCompanyVacancy = saveVacancy(
                savedSecondCompany,
                "Remote Java Intern",
                "https://other.example.com/vacancy",
                null,
                WorkFormat.REMOTE,
                "Other company vacancy"
        );

        Vacancy savedOfficeVacancy = saveVacancy(
                savedFirstCompany,
                "Office Java Intern",
                "https://combined.example.com/office",
                "Oslo",
                WorkFormat.OFFICE,
                "Wrong work format vacancy"
        );

        Application matchingApplication = saveApplication(
                savedMatchingVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Matching application"
        );

        saveApplication(
                savedMatchingVacancy,
                ApplicationStatus.REJECTED,
                appliedAt,
                null,
                "Wrong status"
        );

        saveApplication(
                savedOtherCompanyVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Wrong company"
        );

        saveApplication(
                savedOfficeVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Wrong work format"
        );

        ApplicationFilter filter =
                new ApplicationFilter(
                        ApplicationStatus.INTERVIEW,
                        null,
                        savedFirstCompany.getId(),
                        null,
                        null,
                        WorkFormat.REMOTE,
                        null,
                        null
                );

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(filter);

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(
                matchingApplication.getId(),
                applications.getFirst().getId()
        );
        assertEquals(
                matchingApplication.getStatus(),
                applications.getFirst().getStatus()
        );
        assertEquals(
                savedFirstCompany.getId(),
                applications.getFirst().getVacancy().getCompany().getId()
        );
        assertEquals(
                WorkFormat.REMOTE,
                applications.getFirst().getVacancy().getWorkFormat()
        );
    }

    @Test
    void shouldReturnAllApplicationsWhenFiltersAreMissing() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 6, 10, 0);

        Company savedCompany = saveCompany(
                "No Filter Company",
                "https://no-filter.example.com",
                "Company for unrestricted filtering"
        );

        Vacancy savedVacancy = saveVacancy(
                savedCompany,
                "Java Intern",
                "https://no-filter.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for unrestricted filtering"
        );

        saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "First application"
        );

        saveApplication(
                savedVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Second application"
        );

        ApplicationFilter filter =
                new ApplicationFilter(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(filter);

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(2, applications.size());
    }

    @Test
    void shouldIncludeAppliedAtRangeBoundaries() {
        LocalDateTime appliedAtFrom =
                LocalDateTime.of(2026, 8, 10, 10, 0);

        LocalDateTime insideRange =
                LocalDateTime.of(2026, 8, 15, 10, 0);

        LocalDateTime appliedAtTo =
                LocalDateTime.of(2026, 8, 20, 10, 0);

        LocalDateTime beforeRange =
                LocalDateTime.of(2026, 8, 10, 9, 59);

        LocalDateTime afterRange =
                LocalDateTime.of(2026, 8, 20, 10, 1);

        Company savedCompany = saveCompany(
                "Applied Boundary Company",
                "https://applied-boundary.example.com",
                "Company for appliedAt boundary testing"
        );

        Vacancy savedVacancy = saveVacancy(
                savedCompany,
                "Java Backend Intern",
                "https://applied-boundary.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for appliedAt boundary testing"
        );

        Application beforeApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                beforeRange,
                null,
                "Before appliedAt range"
        );

        Application fromApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAtFrom,
                null,
                "Exactly at appliedAtFrom"
        );

        Application insideApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                insideRange,
                null,
                "Inside appliedAt range"
        );

        Application toApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAtTo,
                null,
                "Exactly at appliedAtTo"
        );

        Application afterApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                afterRange,
                null,
                "After appliedAt range"
        );

        ApplicationFilter filter =
                new ApplicationFilter(
                        null,
                        null,
                        null,
                        appliedAtFrom,
                        appliedAtTo,
                        null,
                        null,
                        null
                );

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(filter);

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(3, applications.size());
        assertTrue(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(fromApplication.getId())
                        )
        );

        assertTrue(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(insideApplication.getId())
                        )
        );

        assertTrue(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(toApplication.getId())
                        )
        );

        assertFalse(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(beforeApplication.getId())
                        )
        );

        assertFalse(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(afterApplication.getId())
                        )
        );
    }

    @Test
    void shouldIncludeNextContactAtRangeBoundaries() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 1, 10, 0);

        LocalDateTime nextContactAtFrom =
                LocalDateTime.of(2026, 8, 10, 10, 0);

        LocalDateTime insideRange =
                LocalDateTime.of(2026, 8, 15, 10, 0);

        LocalDateTime nextContactAtTo =
                LocalDateTime.of(2026, 8, 20, 10, 0);

        LocalDateTime beforeRange =
                LocalDateTime.of(2026, 8, 10, 9, 59);

        LocalDateTime afterRange =
                LocalDateTime.of(2026, 8, 20, 10, 1);

        Company savedCompany = saveCompany(
                "Next Contact Boundary Company",
                "https://next-contact-boundary.example.com",
                "Company for nextContactAt boundary testing"
        );

        Vacancy savedVacancy = saveVacancy(
                savedCompany,
                "Java Backend Intern",
                "https://next-contact-boundary.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for nextContactAt boundary testing"
        );

        Application beforeApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                beforeRange,
                "Before nextContactAt range"
        );

        Application fromApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                nextContactAtFrom,
                "Exactly at nextContactAtFrom"
        );

        Application insideApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                insideRange,
                "Inside nextContactAt range"
        );

        Application toApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                nextContactAtTo,
                "Exactly at nextContactAtTo"
        );

        Application afterApplication = saveApplication(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                afterRange,
                "After nextContactAt range"
        );

        ApplicationFilter filter =
                new ApplicationFilter(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        nextContactAtFrom,
                        nextContactAtTo
                );

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(filter);

        List<Application> applications =
                applicationRepository.findAll(specification);

        assertEquals(3, applications.size());
        assertTrue(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(fromApplication.getId())
                        )
        );

        assertTrue(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(insideApplication.getId())
                        )
        );

        assertTrue(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(toApplication.getId())
                        )
        );

        assertFalse(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(beforeApplication.getId())
                        )
        );

        assertFalse(
                applications.stream()
                        .anyMatch(application ->
                                application.getId().equals(afterApplication.getId())
                        )
        );
    }

    private Company saveCompany(
            String name,
            String website,
            String description
    ) {
        Company company = new Company(
                name,
                website,
                description
        );

        return companyRepository.save(company);
    }

    private Vacancy saveVacancy(
            Company company,
            String title,
            String link,
            String city,
            WorkFormat workFormat,
            String description
    ) {
        Vacancy vacancy = new Vacancy(
                company,
                title,
                link,
                city,
                workFormat,
                description
        );

        return vacancyRepository.save(vacancy);
    }

    private Application saveApplication(
            Vacancy vacancy,
            ApplicationStatus status,
            LocalDateTime appliedAt,
            LocalDateTime nextContactAt,
            String notes
    ) {
        Application application = new Application(
                vacancy,
                status,
                appliedAt,
                nextContactAt,
                notes
        );

        return applicationRepository.save(application);
    }
}
