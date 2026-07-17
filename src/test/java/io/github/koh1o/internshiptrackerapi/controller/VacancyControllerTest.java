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
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void shouldReturnAllVacancies() throws Exception {
        Vacancy firstVacancy = mock(Vacancy.class);
        Vacancy secondVacancy = mock(Vacancy.class);

        List<Vacancy> vacancies = List.of(
                firstVacancy,
                secondVacancy
        );

        VacancyResponse firstResponse = new VacancyResponse(
                10L,
                1L,
                "Example Company",
                "Java Backend Intern",
                "https://example.com/java-intern",
                "Helsinki",
                WorkFormat.HYBRID,
                "Java backend internship",
                LocalDateTime.of(2026, 7, 17, 15, 30),
                LocalDateTime.of(2026, 7, 17, 15, 30)
        );

        VacancyResponse secondResponse = new VacancyResponse(
                20L,
                2L,
                "Remote Tech",
                "Junior Backend Developer",
                "https://remotetech.example/backend-vacancy",
                "Tampere",
                WorkFormat.REMOTE,
                "Remote backend position",
                LocalDateTime.of(2026, 7, 18, 10, 0),
                LocalDateTime.of(2026, 7, 18, 11, 30)
        );

        when(vacancyService.getAllVacancies())
                .thenReturn(vacancies);
        when(vacancyMapper.toResponse(firstVacancy))
                .thenReturn(firstResponse);
        when(vacancyMapper.toResponse(secondVacancy))
                .thenReturn(secondResponse);

        mockMvc.perform(get("/api/vacancies"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[1].id").value(20L))
                .andExpect(jsonPath("$[0].companyName").value("Example Company"))
                .andExpect(jsonPath("$[0].workFormat").value("HYBRID"))
                .andExpect(jsonPath("$[1].companyName").value("Remote Tech"))
                .andExpect(jsonPath("$[1].workFormat").value("REMOTE"));

        verify(vacancyService).getAllVacancies();
        verify(vacancyMapper).toResponse(firstVacancy);
        verify(vacancyMapper).toResponse(secondVacancy);
    }
}
