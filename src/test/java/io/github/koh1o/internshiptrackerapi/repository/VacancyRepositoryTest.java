package io.github.koh1o.internshiptrackerapi.repository;

import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VacancyRepositoryTest {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldInjectVacancyRepository() {
        assertNotNull(vacancyRepository);
    }

    @Test
    void shouldSaveAndFindVacancy() {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Developer tools company"
        );

        Vacancy vacancy = new Vacancy(
                company,
                "Java Backend Intern",
                "https://www.jetbrains.com/careers",
                "Remote",
                WorkFormat.REMOTE,
                "Internship for Java backend developers"
        );

        Company savedCompany = companyRepository.save(company);
        vacancy.setCompany(savedCompany);

        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        Optional<Vacancy> foundVacancy = vacancyRepository.findById(savedVacancy.getId());

        assertTrue(foundVacancy.isPresent());

        assertEquals("Java Backend Intern", foundVacancy.get().getTitle());
        assertEquals(savedCompany.getId(), foundVacancy.get().getCompany().getId());
        assertEquals(WorkFormat.REMOTE, foundVacancy.get().getWorkFormat());
    }
}
