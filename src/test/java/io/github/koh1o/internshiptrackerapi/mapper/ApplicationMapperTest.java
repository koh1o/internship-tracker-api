package io.github.koh1o.internshiptrackerapi.mapper;

import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationResponse;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ApplicationMapperTest {

    private final ApplicationMapper applicationMapper =
            new ApplicationMapper();

    @Test
    void shouldMapRequestToEntity() {
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

        ApplicationRequest request = new ApplicationRequest(
                20L,
                ApplicationStatus.APPLIED,
                appliedAt,
                nextContactAt,
                "Waiting for response"
        );

        Application result = applicationMapper.toEntity(request, vacancy);

        assertSame(vacancy, result.getVacancy());
        assertEquals(ApplicationStatus.APPLIED, result.getStatus());
        assertEquals(appliedAt, result.getAppliedAt());
        assertEquals(nextContactAt, result.getNextContactAt());
        assertEquals("Waiting for response", result.getNotes());
    }

    @Test
    void shouldMapEntityToResponse() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 7, 21, 10, 0);

        LocalDateTime nextContactAt =
                LocalDateTime.of(2026, 7, 28, 10, 0);

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 7, 21, 10, 5);

        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 7, 22, 12, 30);

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

        ReflectionTestUtils.setField(company, "id", 5L);
        ReflectionTestUtils.setField(vacancy, "id", 20L);
        ReflectionTestUtils.setField(application, "id", 30L);
        ReflectionTestUtils.setField(application, "createdAt", createdAt);
        ReflectionTestUtils.setField(application, "updatedAt", updatedAt);

        ApplicationResponse response = applicationMapper.toResponse(application);

        assertEquals(30L, response.id());
        assertEquals(20L, response.vacancyId());
        assertEquals("Java Backend Intern", response.vacancyTitle());
        assertEquals(5L, response.companyId());
        assertEquals("Example Company", response.companyName());
        assertEquals(ApplicationStatus.APPLIED, response.status());
        assertEquals(appliedAt, response.appliedAt());
        assertEquals(nextContactAt, response.nextContactAt());
        assertEquals("Waiting for response", response.notes());
        assertEquals(createdAt, response.createdAt());
        assertEquals(updatedAt, response.updatedAt());
    }
}
