package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyRequest;
import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyResponse;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.VacancyMapper;
import io.github.koh1o.internshiptrackerapi.service.VacancyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(VacancyController.class)
class VacancyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VacancyService vacancyService;

    @MockitoBean
    private VacancyMapper vacancyMapper;

    @Test
    void shouldCreateVacancy() throws Exception {
        Long companyId = 1L;

        VacancyRequest request = new VacancyRequest(
                companyId,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description"
        );

        Vacancy savedVacancy = mock(Vacancy.class);

        LocalDateTime createdAt = LocalDateTime.of(
                2026, 7, 17, 15, 30
        );

        LocalDateTime updatedAt = LocalDateTime.of(
                2026, 7, 17, 15, 30
        );

        VacancyResponse response = new VacancyResponse(
                10L,
                companyId,
                "Example Company",
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description",
                createdAt,
                updatedAt
        );

        String requestJson = """
                {
                  "companyId": 1,
                  "title": "Java Backend Intern",
                  "link": "https://example.com/vacancy",
                  "city": "Helsinki",
                  "workFormat": "HYBRID",
                  "description": "Internship description"
                }
                """;

        when(vacancyService.createVacancy(request))
                .thenReturn(savedVacancy);
        when(vacancyMapper.toResponse(savedVacancy))
                .thenReturn(response);

        mockMvc.perform(post("/api/vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.companyId").value(1L))
                .andExpect(jsonPath("$.companyName").value("Example Company"))
                .andExpect(jsonPath("$.title").value("Java Backend Intern"))
                .andExpect(jsonPath("$.link").value("https://example.com/vacancy"))
                .andExpect(jsonPath("$.city").value("Helsinki"))
                .andExpect(jsonPath("$.workFormat").value("HYBRID"))
                .andExpect(jsonPath("$.description").value("Internship description"))
                .andExpect(jsonPath("$.createdAt").value("2026-07-17T15:30:00"))
                .andExpect(jsonPath("$.updatedAt").value("2026-07-17T15:30:00"));

        verify(vacancyService).createVacancy(request);
        verify(vacancyMapper).toResponse(savedVacancy);
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsBlank() throws Exception {
        String requestJson = """
                {
                  "companyId": 1,
                  "title": "   ",
                  "link": "https://example.com/vacancy",
                  "city": "Helsinki",
                  "workFormat": "HYBRID",
                  "description": "Internship description"
                }
                """;

        mockMvc.perform(post("/api/vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.path").value("/api/vacancies"))
                .andExpect(jsonPath("$.fieldErrors.title").exists());

        verifyNoInteractions(vacancyService, vacancyMapper);
    }

    @Test
    void shouldReturnNotFoundWhenCompanyDoesNotExist() throws Exception {
        Long companyId = 999L;

        VacancyRequest request = new VacancyRequest(
                companyId,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description"
        );

        ResourceNotFoundException exception =
                new ResourceNotFoundException(
                        "Company not found with id: " + companyId
                );

        String requestJson = """
                {
                  "companyId": 999,
                  "title": "Java Backend Intern",
                  "link": "https://example.com/vacancy",
                  "city": "Helsinki",
                  "workFormat": "HYBRID",
                  "description": "Internship description"
                }
                """;

        when(vacancyService.createVacancy(request))
                .thenThrow(exception);

        mockMvc.perform(post("/api/vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message").value("Company not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/vacancies"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(vacancyService).createVacancy(request);
        verifyNoInteractions(vacancyMapper);
    }
}
