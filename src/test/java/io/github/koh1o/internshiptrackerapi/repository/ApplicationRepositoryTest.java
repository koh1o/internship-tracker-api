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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        Company company = new Company(
                "Example Company",
                "https://example.com",
                "Test company"
        );
        Vacancy vacancy = new Vacancy(
                company,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description"
        );
        Application application = new Application(
                vacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                nextContactAt,
                "Waiting for response"
        );

        Company savedCompany = companyRepository.save(company);
        vacancy.setCompany(savedCompany);
        Vacancy savedVacancy = vacancyRepository.save(vacancy);
        application.setVacancy(savedVacancy);
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

        Company company = new Company(
                "Specification Test Company",
                "https://specification.example.com",
                "Company for specification integration test"
        );

        Vacancy vacancy = new Vacancy(
                company,
                "Java Backend Intern",
                "https://specification.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for specification integration test"
        );

        Application interviewApplication = new Application(
                vacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Interview application"
        );

        Application rejectedApplication = new Application(
                vacancy,
                ApplicationStatus.REJECTED,
                appliedAt,
                null,
                "Rejected application"
        );

        Company savedCompany = companyRepository.save(company);

        vacancy.setCompany(savedCompany);

        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        interviewApplication.setVacancy(savedVacancy);
        rejectedApplication.setVacancy(savedVacancy);

        applicationRepository.save(interviewApplication);
        applicationRepository.save(rejectedApplication);

        Specification<Application> specification =
                ApplicationSpecifications.hasStatus(
                        ApplicationStatus.INTERVIEW
                );

        List<Application> applications = applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(ApplicationStatus.INTERVIEW, applications.getFirst().getStatus());
        assertEquals(interviewApplication.getId(), applications.getFirst().getId());
    }

    @Test
    void shouldFilterApplicationsByCompanyId() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 1, 10, 0);

        Company firstCompany = new Company(
                "First Company",
                "https://first.example.com",
                "First company"
        );

        Company secondCompany = new Company(
                "Second Company",
                "https://second.example.com",
                "Second company"
        );

        Company savedFirstCompany =
                companyRepository.save(firstCompany);

        Company savedSecondCompany =
                companyRepository.save(secondCompany);

        Vacancy firstVacancy = new Vacancy(
                savedFirstCompany,
                "First Java Intern",
                "https://first.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "First vacancy"
        );

        Vacancy secondVacancy = new Vacancy(
                savedSecondCompany,
                "Second Java Intern",
                "https://second.example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Second vacancy"
        );

        Vacancy savedFirstVacancy =
                vacancyRepository.save(firstVacancy);

        Vacancy savedSecondVacancy =
                vacancyRepository.save(secondVacancy);

        Application firstApplication = new Application(
                savedFirstVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "First application"
        );

        Application secondApplication = new Application(
                savedSecondVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Second application"
        );

        applicationRepository.save(firstApplication);
        applicationRepository.save(secondApplication);

        Specification<Application> specification =
                ApplicationSpecifications.hasCompanyId(
                        savedFirstCompany.getId()
                );

        List<Application> applications = applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(firstApplication.getId(), applications.getFirst().getId());
        assertEquals(
                savedFirstCompany.getId(),
                applications.getFirst().getVacancy().getCompany().getId()
        );
    }

    @Test
    void shouldFilterApplicationsByWorkFormat() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 2, 10, 0);

        Company company = new Company(
                "Work Format Company",
                "https://work-format.example.com",
                "Company for work format filtering"
        );

        Company savedCompany =
                companyRepository.save(company);

        Vacancy remoteVacancy = new Vacancy(
                savedCompany,
                "Remote Java Intern",
                "https://work-format.example.com/remote",
                null,
                WorkFormat.REMOTE,
                "Remote vacancy"
        );

        Vacancy officeVacancy = new Vacancy(
                savedCompany,
                "Office Java Intern",
                "https://work-format.example.com/office",
                "Oslo",
                WorkFormat.OFFICE,
                "Office vacancy"
        );

        Vacancy savedRemoteVacancy =
                vacancyRepository.save(remoteVacancy);

        Vacancy savedOfficeVacancy =
                vacancyRepository.save(officeVacancy);

        Application remoteApplication = new Application(
                savedRemoteVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Remote application"
        );

        Application officeApplication = new Application(
                savedOfficeVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Office application"
        );

        applicationRepository.save(remoteApplication);
        applicationRepository.save(officeApplication);

        Specification<Application> specification =
                ApplicationSpecifications.hasWorkFormat(
                        WorkFormat.REMOTE
                );

        List<Application> applications = applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(remoteApplication.getId(), applications.getFirst().getId());
        assertEquals(WorkFormat.REMOTE, applications.getFirst().getVacancy().getWorkFormat());
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

        Company company = new Company(
                "Date Filter Company",
                "https://date-filter.example.com",
                "Company for date filtering"
        );

        Company savedCompany =
                companyRepository.save(company);

        Vacancy vacancy = new Vacancy(
                savedCompany,
                "Java Intern",
                "https://date-filter.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for date filtering"
        );

        Vacancy savedVacancy =
                vacancyRepository.save(vacancy);

        Application beforeApplication = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                beforeRange,
                null,
                "Before range"
        );

        Application insideApplication = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                insideRange,
                null,
                "Inside range"
        );

        Application afterApplication = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                afterRange,
                null,
                "After range"
        );

        applicationRepository.save(beforeApplication);
        applicationRepository.save(insideApplication);
        applicationRepository.save(afterApplication);

        Specification<Application> specification =
                ApplicationSpecifications.withFilters(
                        null,
                        null,
                        null,
                        appliedAtFrom,
                        appliedAtTo
                );

        List<Application> applications = applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(insideApplication.getId(), applications.getFirst().getId());
        assertEquals(insideRange, applications.getFirst().getAppliedAt());
    }

    @Test
    void shouldFilterApplicationsByVacancyId() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 3, 10, 0);

        Company company = new Company(
                "Vacancy Filter Company",
                "https://vacancy-filter.example.com",
                "Company for vacancy filtering"
        );

        Company savedCompany =
                companyRepository.save(company);

        Vacancy firstVacancy = new Vacancy(
                savedCompany,
                "First Java Intern",
                "https://vacancy-filter.example.com/first",
                "Oslo",
                WorkFormat.HYBRID,
                "First vacancy"
        );

        Vacancy secondVacancy = new Vacancy(
                savedCompany,
                "Second Java Intern",
                "https://vacancy-filter.example.com/second",
                "Oslo",
                WorkFormat.HYBRID,
                "Second vacancy"
        );

        Vacancy savedFirstVacancy =
                vacancyRepository.save(firstVacancy);

        Vacancy savedSecondVacancy =
                vacancyRepository.save(secondVacancy);

        Application firstApplication = new Application(
                savedFirstVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "First application"
        );

        Application secondApplication = new Application(
                savedSecondVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Second application"
        );

        applicationRepository.save(firstApplication);
        applicationRepository.save(secondApplication);

        Specification<Application> specification =
                ApplicationSpecifications.hasVacancyId(
                        savedFirstVacancy.getId()
                );

        List<Application> applications = applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(firstApplication.getId(), applications.getFirst().getId());
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

        Company company = new Company(
                "Next Contact Company",
                "https://next-contact.example.com",
                "Company for next contact filtering"
        );

        Company savedCompany =
                companyRepository.save(company);

        Vacancy vacancy = new Vacancy(
                savedCompany,
                "Java Backend Intern",
                "https://next-contact.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for next contact filtering"
        );

        Vacancy savedVacancy =
                vacancyRepository.save(vacancy);

        Application beforeApplication = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                beforeRange,
                "Before next contact range"
        );

        Application insideApplication = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                insideRange,
                "Inside next contact range"
        );

        Application afterApplication = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                afterRange,
                "After next contact range"
        );

        applicationRepository.save(beforeApplication);
        applicationRepository.save(insideApplication);
        applicationRepository.save(afterApplication);

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

        List<Application> applications = applicationRepository.findAll(specification);
        assertEquals(1, applications.size());
        assertEquals(insideApplication.getId(), applications.getFirst().getId());
        assertEquals(insideRange, applications.getFirst().getNextContactAt());
    }

    @Test
    void shouldFilterApplicationsByMultipleFilters() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 5, 10, 0);

        Company firstCompany = new Company(
                "Combined Filter Company",
                "https://combined.example.com",
                "Matching company"
        );

        Company secondCompany = new Company(
                "Other Company",
                "https://other.example.com",
                "Non-matching company"
        );

        Company savedFirstCompany =
                companyRepository.save(firstCompany);

        Company savedSecondCompany =
                companyRepository.save(secondCompany);

        Vacancy matchingVacancy = new Vacancy(
                savedFirstCompany,
                "Remote Java Intern",
                "https://combined.example.com/vacancy",
                null,
                WorkFormat.REMOTE,
                "Matching vacancy"
        );

        Vacancy otherCompanyVacancy = new Vacancy(
                savedSecondCompany,
                "Remote Java Intern",
                "https://other.example.com/vacancy",
                null,
                WorkFormat.REMOTE,
                "Other company vacancy"
        );

        Vacancy savedMatchingVacancy =
                vacancyRepository.save(matchingVacancy);

        Vacancy savedOtherCompanyVacancy =
                vacancyRepository.save(otherCompanyVacancy);

        Application matchingApplication = new Application(
                savedMatchingVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Matching application"
        );

        Application wrongStatusApplication = new Application(
                savedMatchingVacancy,
                ApplicationStatus.REJECTED,
                appliedAt,
                null,
                "Wrong status"
        );

        Application wrongCompanyApplication = new Application(
                savedOtherCompanyVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Wrong company"
        );

        Vacancy officeVacancy = new Vacancy(
                savedFirstCompany,
                "Office Java Intern",
                "https://combined.example.com/office",
                "Oslo",
                WorkFormat.OFFICE,
                "Wrong work format vacancy"
        );

        Vacancy savedOfficeVacancy =
                vacancyRepository.save(officeVacancy);

        Application wrongWorkFormatApplication = new Application(
                savedOfficeVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Wrong work format"
        );

        applicationRepository.save(wrongWorkFormatApplication);
        applicationRepository.save(matchingApplication);
        applicationRepository.save(wrongStatusApplication);
        applicationRepository.save(wrongCompanyApplication);

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

        List<Application> applications = applicationRepository.findAll(specification);

        assertEquals(1, applications.size());
        assertEquals(matchingApplication.getId(), applications.getFirst().getId());
        assertEquals(matchingApplication.getStatus(), applications.getFirst().getStatus());
        assertEquals(savedFirstCompany.getId(), applications.getFirst().getVacancy().getCompany().getId());
        assertEquals(WorkFormat.REMOTE, applications.getFirst().getVacancy().getWorkFormat());
    }

    @Test
    void shouldReturnAllApplicationsWhenFiltersAreMissing() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 6, 10, 0);

        Company company = new Company(
                "No Filter Company",
                "https://no-filter.example.com",
                "Company for unrestricted filtering"
        );

        Company savedCompany =
                companyRepository.save(company);

        Vacancy vacancy = new Vacancy(
                savedCompany,
                "Java Intern",
                "https://no-filter.example.com/vacancy",
                "Oslo",
                WorkFormat.HYBRID,
                "Vacancy for unrestricted filtering"
        );

        Vacancy savedVacancy =
                vacancyRepository.save(vacancy);

        Application firstApplication = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "First application"
        );

        Application secondApplication = new Application(
                savedVacancy,
                ApplicationStatus.INTERVIEW,
                appliedAt,
                null,
                "Second application"
        );

        applicationRepository.save(firstApplication);
        applicationRepository.save(secondApplication);

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

        List<Application> applications = applicationRepository.findAll(specification);

        assertEquals(2, applications.size());
    }
}
