package io.github.koh1o.internshiptrackerapi.integration;

import io.github.koh1o.internshiptrackerapi.configuration.TestcontainersConfiguration;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import io.github.koh1o.internshiptrackerapi.repository.ApplicationRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void cleanDatabase() {
        applicationRepository.deleteAll();
        vacancyRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    void shouldCreateApplication() throws Exception {
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

        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 10, 10, 0);

        LocalDateTime nextContactAt =
                LocalDateTime.of(2026, 8, 15, 12, 0);

        String requestBody = """
                {
                  "vacancyId": %d,
                  "status": "APPLIED",
                  "appliedAt": "2026-08-10T10:00:00",
                  "nextContactAt": "2026-08-15T12:00:00",
                  "notes": "Applied through careers page"
                }
                """.formatted(savedVacancy.getId());

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.vacancyId").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.vacancyTitle").value(savedVacancy.getTitle()))
                .andExpect(jsonPath("$.companyId").value(savedCompany.getId()))
                .andExpect(jsonPath("$.companyName").value(savedCompany.getName()))
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.appliedAt")
                        .value("2026-08-10T10:00:00"))
                .andExpect(jsonPath("$.nextContactAt")
                        .value("2026-08-15T12:00:00"))
                .andExpect(jsonPath("$.notes").value("Applied through careers page"));

        List<Application> applications =
                applicationRepository.findAll();

        assertEquals(1, applications.size());

        Application savedApplication =
                applications.getFirst();

        assertNotNull(savedApplication.getId());
        assertEquals(ApplicationStatus.APPLIED, savedApplication.getStatus());
        assertEquals(appliedAt, savedApplication.getAppliedAt());
        assertEquals(nextContactAt, savedApplication.getNextContactAt());
        assertEquals("Applied through careers page", savedApplication.getNotes());
        assertEquals(savedVacancy.getId(), savedApplication.getVacancy().getId());
    }

    @Test
    void shouldGetApplicationById() throws Exception {
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

        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 10, 10, 0);

        LocalDateTime nextContactAt =
                LocalDateTime.of(2026, 8, 15, 12, 0);

        Application application = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                nextContactAt,
                "Applied through careers page"
        );

        Application savedApplication =
                applicationRepository.save(application);

        mockMvc.perform(get("/api/applications/{id}", savedApplication.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedApplication.getId()))
                .andExpect(jsonPath("$.vacancyId").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.vacancyTitle").value("Java Backend Intern"))
                .andExpect(jsonPath("$.companyId").value(savedCompany.getId()))
                .andExpect(jsonPath("$.companyName").value("JetBrains"))
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.appliedAt").value("2026-08-10T10:00:00"))
                .andExpect(jsonPath("$.nextContactAt").value("2026-08-15T12:00:00"))
                .andExpect(jsonPath("$.notes").value("Applied through careers page"));
    }

    @Test
    void shouldReturnNotFoundWhenApplicationDoesNotExist() throws Exception {
        long missingApplicationId = 999999L;

        mockMvc.perform(get("/api/applications/{id}", missingApplicationId))
                .andExpect(status().isNotFound());

        assertEquals(0, applicationRepository.count());
    }

    @Test
    void shouldReturnNotFoundWhenCreatingApplicationForMissingVacancy()
            throws Exception {

        long missingVacancyId = 999999L;

        String requestBody = """
                {
                  "vacancyId": %d,
                  "status": "APPLIED",
                  "appliedAt": "2026-08-10T10:00:00",
                  "nextContactAt": "2026-08-15T12:00:00",
                  "notes": "Application for missing vacancy"
                }
                """.formatted(missingVacancyId);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());

        assertEquals(0, applicationRepository.count());
    }

    @Test
    void shouldRejectAppliedApplicationWithoutAppliedAt() throws Exception {
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

        String requestBody = """
                {
                  "vacancyId": %d,
                  "status": "APPLIED",
                  "appliedAt": null,
                  "nextContactAt": null,
                  "notes": "Missing applied date"
                }
                """.formatted(savedVacancy.getId());

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        assertEquals(0, applicationRepository.count());
    }

    @Test
    void shouldRejectApplicationWhenNextContactAtIsBeforeAppliedAt()
            throws Exception {

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

        String requestBody = """
                {
                  "vacancyId": %d,
                  "status": "APPLIED",
                  "appliedAt": "2026-08-15T12:00:00",
                  "nextContactAt": "2026-08-10T10:00:00",
                  "notes": "Invalid dates"
                }
                """.formatted(savedVacancy.getId());

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        assertEquals(0, applicationRepository.count());
    }

    @Test
    void shouldUpdateApplicationStatus() throws Exception {
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

        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 8, 10, 10, 0);

        Application application = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                appliedAt,
                null,
                "Waiting for interview"
        );

        Application savedApplication =
                applicationRepository.save(application);

        String requestBody = """
                {
                  "status": "INTERVIEW"
                }
                """;

        mockMvc.perform(patch("/api/applications/{id}/status", savedApplication.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").value(savedApplication.getId()))
                .andExpect(jsonPath("$.status").value("INTERVIEW"));

        Optional<Application> updatedStatusApplication =
                applicationRepository.findById(savedApplication.getId());

        assertTrue(updatedStatusApplication.isPresent());
        assertEquals(ApplicationStatus.INTERVIEW, updatedStatusApplication.get().getStatus());
        assertEquals(savedApplication.getId(), updatedStatusApplication.get().getId());
    }

    @Test
    void shouldRejectInvalidStatusTransition() throws Exception {
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

        Application application = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 8, 10, 10, 0),
                null,
                "Applied"
        );

        Application savedApplication =
                applicationRepository.save(application);

        String requestBody = """
                {
                  "status": "OFFER"
                }
                """;

        mockMvc.perform(patch("/api/applications/{id}/status", savedApplication.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        Optional<Application> applicationOptional =
                applicationRepository.findById(savedApplication.getId());

        assertTrue(applicationOptional.isPresent());
        assertEquals(ApplicationStatus.APPLIED, applicationOptional.get().getStatus());
    }

    @Test
    void shouldAllowSettingSameApplicationStatus() throws Exception {
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

        Application application = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 8, 10, 10, 0),
                null,
                "Applied"
        );

        Application savedApplication =
                applicationRepository.save(application);

        String requestBody = """
                {
                  "status": "APPLIED"
                }
                """;

        mockMvc.perform(patch("/api/applications/{id}/status", savedApplication.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("$.id").value(savedApplication.getId()))
                .andExpect(jsonPath("$.status").value("APPLIED"));

        List<Application> applications = applicationRepository.findAll();

        assertEquals(1, applications.size());
        assertEquals(ApplicationStatus.APPLIED, applications.getFirst().getStatus());
    }

    @Test
    void shouldUpdateApplication() throws Exception {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software company"
        );

        Company savedCompany = companyRepository.save(company);

        Vacancy firstVacancy = new Vacancy(
                savedCompany,
                "Java Backend Intern",
                "https://example.com/first",
                "Prague",
                WorkFormat.REMOTE,
                "First vacancy"
        );

        Vacancy savedFirstVacancy =
                vacancyRepository.save(firstVacancy);

        Vacancy secondVacancy = new Vacancy(
                savedCompany,
                "Updated Backend Intern",
                "https://example.com/second",
                "Berlin",
                WorkFormat.HYBRID,
                "Second vacancy"
        );

        Vacancy savedSecondVacancy =
                vacancyRepository.save(secondVacancy);

        Application application = new Application(
                savedFirstVacancy,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 8, 10, 10, 0),
                LocalDateTime.of(2026, 8, 15, 12, 0),
                "Old notes"
        );

        Application savedApplication =
                applicationRepository.save(application);

        String requestBody = """
                {
                  "vacancyId": %d,
                  "appliedAt": "2026-08-11T11:00:00",
                  "nextContactAt": "2026-08-20T15:00:00",
                  "notes": "Updated notes"
                }
                """.formatted(savedSecondVacancy.getId());

        mockMvc.perform(put("/api/applications/{id}", savedApplication.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedApplication.getId()))
                .andExpect(jsonPath("$.vacancyId").value(savedSecondVacancy.getId()))
                .andExpect(jsonPath("$.vacancyTitle").value(savedSecondVacancy.getTitle()))
                .andExpect(jsonPath("$.companyId").value(savedCompany.getId()))
                .andExpect(jsonPath("$.companyName").value(savedCompany.getName()))
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.appliedAt").value("2026-08-11T11:00:00"))
                .andExpect(jsonPath("$.nextContactAt").value("2026-08-20T15:00:00"))
                .andExpect(jsonPath("$.notes").value("Updated notes"));

        Optional<Application> foundApplication =
                applicationRepository.findById(savedApplication.getId());
        assertTrue(foundApplication.isPresent());
        Application presentApplication = foundApplication.get();
        assertEquals(savedApplication.getId(), presentApplication.getId());
        assertEquals(savedSecondVacancy.getId(), presentApplication.getVacancy().getId());
        assertEquals(
                ApplicationStatus.APPLIED,
                presentApplication.getStatus()
        );
        assertEquals(
                LocalDateTime.of(2026, 8, 11, 11, 0),
                presentApplication.getAppliedAt()
        );

        assertEquals(
                LocalDateTime.of(2026, 8, 20, 15, 0),
                presentApplication.getNextContactAt()
        );
        assertEquals("Updated notes", presentApplication.getNotes());
    }

    @Test
    void shouldDeleteApplication() throws Exception {
        Company company = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software company"
        );

        Company savedCompany =
                companyRepository.save(company);

        Vacancy vacancy = new Vacancy(
                savedCompany,
                "Java Backend Intern",
                "https://www.jetbrains.com/careers",
                "Prague",
                WorkFormat.REMOTE,
                "Backend internship"
        );

        Vacancy savedVacancy =
                vacancyRepository.save(vacancy);

        Application application = new Application(
                savedVacancy,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 8, 10, 10, 0),
                null,
                "Application to delete"
        );

        Application savedApplication =
                applicationRepository.save(application);

        Long applicationId = savedApplication.getId();
        Long vacancyId = savedVacancy.getId();
        Long companyId = savedCompany.getId();

        mockMvc.perform(delete("/api/applications/{id}", applicationId))
                .andExpect(status().isNoContent());

        assertFalse(applicationRepository.existsById(applicationId));
        assertTrue(vacancyRepository.existsById(vacancyId));
        assertTrue(companyRepository.existsById(companyId));
    }
}