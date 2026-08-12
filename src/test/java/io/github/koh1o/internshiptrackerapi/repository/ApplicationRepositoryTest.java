package io.github.koh1o.internshiptrackerapi.repository;

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
}
