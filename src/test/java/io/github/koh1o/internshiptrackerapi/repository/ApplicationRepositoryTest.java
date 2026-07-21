package io.github.koh1o.internshiptrackerapi.repository;

import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDateTime;

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
}
