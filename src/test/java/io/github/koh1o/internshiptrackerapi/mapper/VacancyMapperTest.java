package io.github.koh1o.internshiptrackerapi.mapper;

import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyRequest;
import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyResponse;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VacancyMapperTest {

    private final VacancyMapper vacancyMapper = new VacancyMapper();

    @Test
    void shouldMapRequestToEntity() {
        Company company = new Company(
                "Example Company",
                "https://example.com",
                "Description"
        );

        VacancyRequest request = new VacancyRequest(
                1L,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description"
        );

        Vacancy vacancy = vacancyMapper.toEntity(request, company);

        assertSame(company, vacancy.getCompany());
        assertEquals("Java Backend Intern", vacancy.getTitle());
        assertEquals("https://example.com/vacancy", vacancy.getLink());
        assertEquals("Helsinki", vacancy.getCity());
        assertEquals(WorkFormat.HYBRID, vacancy.getWorkFormat());
        assertEquals("Internship description", vacancy.getDescription());
    }

    @Test
    void shouldMapEntityToResponse() {
        Company company = mock(Company.class);
        Vacancy vacancy = mock(Vacancy.class);

        Long vacancyId = 10L;
        Long companyId = 1L;

        LocalDateTime createdAt =
                LocalDateTime.of(2026, 7, 13, 10, 0);

        LocalDateTime updatedAt =
                LocalDateTime.of(2026, 7, 13, 12, 30);

        when(company.getId()).thenReturn(companyId);
        when(company.getName()).thenReturn("JetBrains");

        when(vacancy.getId()).thenReturn(vacancyId);
        when(vacancy.getCompany()).thenReturn(company);
        when(vacancy.getTitle()).thenReturn("Java Backend Intern");
        when(vacancy.getLink()).thenReturn("https://example.com/vacancy");
        when(vacancy.getCity()).thenReturn("Helsinki");
        when(vacancy.getWorkFormat()).thenReturn(WorkFormat.HYBRID);
        when(vacancy.getDescription()).thenReturn("Internship description");
        when(vacancy.getCreatedAt()).thenReturn(createdAt);
        when(vacancy.getUpdatedAt()).thenReturn(updatedAt);

        VacancyResponse response = vacancyMapper.toResponse(vacancy);

        assertEquals(vacancyId, response.id());
        assertEquals(companyId, response.companyId());
        assertEquals("JetBrains", response.companyName());
        assertEquals("Java Backend Intern", response.title());
        assertEquals("https://example.com/vacancy", response.link());
        assertEquals("Helsinki", response.city());
        assertEquals(WorkFormat.HYBRID, response.workFormat());
        assertEquals("Internship description", response.description());
        assertEquals(createdAt, response.createdAt());
        assertEquals(updatedAt, response.updatedAt());
    }

    @Test
    void shouldUpdateEntity() {
        Company oldCompany = new Company(
                "Old Company",
                "https://old-company.example",
                "Old company description"
        );

        Company newCompany = new Company(
                "New Company",
                "https://new-company.example",
                "New company description"
        );

        Vacancy vacancy = new Vacancy(
                oldCompany,
                "Java Backend Intern",
                "https://example.com/java-intern",
                "Helsinki",
                WorkFormat.HYBRID,
                "Original internship description"
        );

        VacancyRequest request = new VacancyRequest(
                2L,
                "Updated Backend Internship",
                "https://example.com/updated-vacancy",
                "Tampere",
                WorkFormat.REMOTE,
                "Updated description"
        );

        vacancyMapper.updateEntity(vacancy, request, newCompany);

        assertSame(newCompany, vacancy.getCompany());
        assertEquals("Updated Backend Internship", vacancy.getTitle());
        assertEquals("https://example.com/updated-vacancy", vacancy.getLink());
        assertEquals("Tampere", vacancy.getCity());
        assertEquals(WorkFormat.REMOTE, vacancy.getWorkFormat());
        assertEquals("Updated description", vacancy.getDescription());
    }
}
