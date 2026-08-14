package io.github.koh1o.internshiptrackerapi.integration;

import io.github.koh1o.internshiptrackerapi.configuration.TestcontainersConfiguration;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class VacancyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void cleanDatabase() {
        vacancyRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    void shouldCreateVacancy() throws Exception {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software company"
        );

        Company savedCompany = companyRepository.save(company);

        String requestBody = """
                {
                  "companyId": %d,
                  "title": "Java Backend Intern",
                  "link": "https://www.jetbrains.com/careers",
                  "city": "Prague",
                  "workFormat": "REMOTE",
                  "description": "Backend internship"
                }
                """.formatted(savedCompany.getId());

        mockMvc.perform(post("/api/vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.companyId").value(savedCompany.getId()))
                .andExpect(jsonPath("$.companyName").value("JetBrains"))
                .andExpect(jsonPath("$.title").value("Java Backend Intern"))
                .andExpect(jsonPath("$.link").value("https://www.jetbrains.com/careers"))
                .andExpect(jsonPath("$.city").value("Prague"))
                .andExpect(jsonPath("$.workFormat").value("REMOTE"))
                .andExpect(jsonPath("$.description").value("Backend internship"));

        List<Vacancy> vacancies = vacancyRepository.findAll();

        assertEquals(1, vacancies.size());

        Vacancy savedVacancy = vacancies.getFirst();

        assertNotNull(savedVacancy.getId());
        assertEquals("Java Backend Intern", savedVacancy.getTitle());
        assertEquals(
                "https://www.jetbrains.com/careers",
                savedVacancy.getLink()
        );
        assertEquals("Prague", savedVacancy.getCity());
        assertEquals(
                WorkFormat.REMOTE,
                savedVacancy.getWorkFormat()
        );
        assertEquals(
                "Backend internship",
                savedVacancy.getDescription()
        );
        assertEquals(
                savedCompany.getId(),
                savedVacancy.getCompany().getId()
        );
    }

    @Test
    void shouldGetVacancyById() throws Exception {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software company"
        );

        Company savedCompany = companyRepository.save(company);

        Vacancy vacancy = new Vacancy(
                savedCompany,
                "Java Backend Intern",
                "https://www.jetbrains.com/careers",
                "Prague",
                WorkFormat.REMOTE,
                "Backend internship"
        );

        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        mockMvc.perform(get("/api/vacancies/{id}", savedVacancy.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.companyId").value(savedCompany.getId()))
                .andExpect(jsonPath("$.companyName").value("JetBrains"))
                .andExpect(jsonPath("$.title").value("Java Backend Intern"))
                .andExpect(jsonPath("$.link").value("https://www.jetbrains.com/careers"))
                .andExpect(jsonPath("$.city").value("Prague"))
                .andExpect(jsonPath("$.workFormat").value("REMOTE"))
                .andExpect(jsonPath("$.description").value("Backend internship"));
    }

    @Test
    void shouldReturnNotFoundWhenVacancyDoesNotExist() throws Exception {
        long missingVacancyId = 999999L;

        mockMvc.perform(get("/api/vacancies/{id}", missingVacancyId))
                .andExpect(status().isNotFound());

        assertEquals(0, vacancyRepository.count());
    }

    @Test
    void shouldReturnNotFoundWhenCreatingVacancyForMissingCompany() throws Exception {
        long missingCompanyId = 999999L;

        String requestBody = """
                {
                  "companyId": %d,
                  "title": "Java Backend Intern",
                  "link": "https://example.com/vacancy",
                  "city": "Prague",
                  "workFormat": "REMOTE",
                  "description": "Backend internship"
                }
                """.formatted(missingCompanyId);

        mockMvc.perform(post("/api/vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());

        assertEquals(0, vacancyRepository.count());
    }

    @Test
    void shouldUpdateVacancy() throws Exception {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software company"
        );

        Company savedCompany = companyRepository.save(company);

        Vacancy vacancy = new Vacancy(
                savedCompany,
                "Old Java Intern",
                "https://old.example.com",
                "Prague",
                WorkFormat.OFFICE,
                "Old description"
        );

        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        String requestBody = """
                {
                  "companyId": %d,
                  "title": "Updated Java Backend Intern",
                  "link": "https://updated.example.com",
                  "city": "Berlin",
                  "workFormat": "HYBRID",
                  "description": "Updated description"
                }
                """.formatted(savedCompany.getId());

        mockMvc.perform(
                        put(
                                "/api/vacancies/{id}",
                                savedVacancy.getId()
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.companyId").value(savedCompany.getId()))
                .andExpect(jsonPath("$.companyName").value("JetBrains"))
                .andExpect(jsonPath("$.title").value("Updated Java Backend Intern"))
                .andExpect(jsonPath("$.link").value("https://updated.example.com"))
                .andExpect(jsonPath("$.city").value("Berlin"))
                .andExpect(jsonPath("$.workFormat").value("HYBRID"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        Optional<Vacancy> optionalVacancy =
                vacancyRepository.findById(savedVacancy.getId());

        assertTrue(optionalVacancy.isPresent());

        Vacancy updatedVacancy = optionalVacancy.get();

        assertEquals(
                "Updated Java Backend Intern",
                updatedVacancy.getTitle()
        );
        assertEquals(
                "https://updated.example.com",
                updatedVacancy.getLink()
        );
        assertEquals("Berlin", updatedVacancy.getCity());
        assertEquals(
                WorkFormat.HYBRID,
                updatedVacancy.getWorkFormat()
        );
        assertEquals(
                "Updated description",
                updatedVacancy.getDescription()
        );
        assertEquals(
                savedCompany.getId(),
                updatedVacancy.getCompany().getId()
        );
    }

    @Test
    void shouldRejectVacancyCreationWithBlankTitle() throws Exception {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software company"
        );

        Company savedCompany = companyRepository.save(company);

        String requestBody = """
                {
                  "companyId": %d,
                  "title": "",
                  "link": "https://example.com/vacancy",
                  "city": "Prague",
                  "workFormat": "REMOTE",
                  "description": "Invalid vacancy"
                }
                """.formatted(savedCompany.getId());

        mockMvc.perform(post("/api/vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        assertEquals(0, vacancyRepository.count());
    }

    @Test
    void shouldDeleteVacancy() throws Exception {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software company"
        );

        Company savedCompany = companyRepository.save(company);

        Vacancy vacancy = new Vacancy(
                savedCompany,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Prague",
                WorkFormat.REMOTE,
                "Vacancy to delete"
        );

        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        Long vacancyId = savedVacancy.getId();

        mockMvc.perform(delete("/api/vacancies/{id}", vacancyId))
                .andExpect(status().isNoContent());

        assertFalse(vacancyRepository.existsById(vacancyId));
        assertTrue(companyRepository.existsById(savedCompany.getId()));
    }
}