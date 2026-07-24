package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.dto.PagedResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationStatusUpdateRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationUpdateRequest;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.exception.InvalidApplicationDataException;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.ApplicationMapper;
import io.github.koh1o.internshiptrackerapi.service.ApplicationService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ApplicationService applicationService;

    @MockitoBean
    private ApplicationMapper applicationMapper;

    @Test
    void shouldCreateApplication() throws Exception {
        ApplicationRequest request = new ApplicationRequest(
                20L,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Waiting for response"
        );

        Application savedApplication = mock(Application.class);

        ApplicationResponse response = new ApplicationResponse(
                30L,
                20L,
                "Java Backend Intern",
                5L,
                "Example Company",
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Waiting for response",
                LocalDateTime.of(2026, 7, 21, 10, 5),
                LocalDateTime.of(2026, 7, 21, 10, 5)
        );

        when(applicationService.createApplication(request))
                .thenReturn(savedApplication);

        when(applicationMapper.toResponse(savedApplication))
                .thenReturn(response);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.vacancyId").value(20L))
                .andExpect(jsonPath("$.vacancyTitle").value("Java Backend Intern"))
                .andExpect(jsonPath("$.companyId").value(5L))
                .andExpect(jsonPath("$.companyName").value("Example Company"))
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.appliedAt").value("2026-07-21T10:00:00"))
                .andExpect(jsonPath("$.nextContactAt").value("2026-07-28T10:00:00"))
                .andExpect(jsonPath("$.notes").value("Waiting for response"))
                .andExpect(jsonPath("$.createdAt").value("2026-07-21T10:05:00"))
                .andExpect(jsonPath("$.updatedAt").value("2026-07-21T10:05:00"));

        verify(applicationService).createApplication(request);
        verify(applicationMapper).toResponse(savedApplication);
    }

    @Test
    void shouldReturnNotFoundWhenCreatingForMissingVacancy()
            throws Exception {

        ApplicationRequest request = new ApplicationRequest(
                999L,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Waiting for response"
        );

        ResourceNotFoundException exception =
                new ResourceNotFoundException(
                        "Vacancy not found with id: 999"
                );

        when(applicationService.createApplication(request))
                .thenThrow(exception);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message")
                        .value("Vacancy not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).createApplication(request);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnBadRequestWhenVacancyIdIsMissing()
            throws Exception {

        String requestJson = """
                {
                  "status": "APPLIED",
                  "appliedAt": "2026-07-21T10:00:00",
                  "nextContactAt": "2026-07-28T10:00:00",
                  "notes": "Waiting for response"
                }
                """;

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors.vacancyId")
                        .value("Vacancy id is required"));

        verifyNoInteractions(applicationService, applicationMapper);
    }

    @Test
    void shouldGetPagedApplications() throws Exception {
        int page = 0;
        int size = 2;
        String sortBy = "appliedAt";
        String direction = "ASC";
        long totalElements = 5;
        int totalPages = 3;

        LocalDateTime firstAppliedAt = LocalDateTime.of(2026, 7, 20, 10, 0);
        LocalDateTime firstNextContactAt = LocalDateTime.of(2026, 7, 25, 12, 0);
        LocalDateTime firstCreatedAt = LocalDateTime.of(2026, 7, 20, 10, 5);
        LocalDateTime firstUpdatedAt = LocalDateTime.of(2026, 7, 20, 10, 5);

        LocalDateTime secondAppliedAt = LocalDateTime.of(2026, 7, 21, 11, 0);
        LocalDateTime secondNextContactAt = LocalDateTime.of(2026, 7, 28, 14, 0);
        LocalDateTime secondCreatedAt = LocalDateTime.of(2026, 7, 21, 11, 5);
        LocalDateTime secondUpdatedAt = LocalDateTime.of(2026, 7, 22, 9, 0);

        ApplicationResponse firstResponse = new ApplicationResponse(
                1L,
                10L,
                "Java Backend Intern",
                100L,
                "First Company",
                ApplicationStatus.APPLIED,
                firstAppliedAt,
                firstNextContactAt,
                "Waiting for response",
                firstCreatedAt,
                firstUpdatedAt
        );

        ApplicationResponse secondResponse = new ApplicationResponse(
                2L,
                20L,
                "Spring Boot Intern",
                200L,
                "Second Company",
                ApplicationStatus.INTERVIEW,
                secondAppliedAt,
                secondNextContactAt,
                "Technical interview scheduled",
                secondCreatedAt,
                secondUpdatedAt
        );

        List<ApplicationResponse> content = List.of(
                firstResponse,
                secondResponse
        );

        PagedResponse<ApplicationResponse> pagedResponse =
                new PagedResponse<>(
                        content,
                        page,
                        size,
                        totalElements,
                        totalPages
                );

        when(applicationService.getAllApplications(
                page,
                size,
                sortBy,
                direction,
                null,
                null
        )).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(firstResponse.id()))
                .andExpect(jsonPath("$.content[0].status")
                        .value(firstResponse.status().name()))
                .andExpect(jsonPath("$.content[1].id").value(secondResponse.id()))
                .andExpect(jsonPath("$.content[1].status")
                        .value(secondResponse.status().name()))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages));

        verify(applicationService).getAllApplications(
                page,
                size,
                sortBy,
                direction,
                null,
                null
        );
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldGetApplicationById() throws Exception {
        Long applicationId = 30L;

        Application application = mock(Application.class);

        ApplicationResponse response = new ApplicationResponse(
                30L,
                20L,
                "Java Backend Intern",
                5L,
                "Example Company",
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Waiting for response",
                LocalDateTime.of(2026, 7, 21, 10, 5),
                LocalDateTime.of(2026, 7, 21, 10, 5)
        );

        when(applicationService.getApplicationById(applicationId))
                .thenReturn(application);
        when(applicationMapper.toResponse(application))
                .thenReturn(response);

        mockMvc.perform(get("/api/applications/{id}", applicationId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.vacancyId").value(20L))
                .andExpect(jsonPath("$.vacancyTitle").value("Java Backend Intern"))
                .andExpect(jsonPath("$.companyId").value(5L))
                .andExpect(jsonPath("$.companyName").value("Example Company"))
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.appliedAt").value("2026-07-21T10:00:00"))
                .andExpect(jsonPath("$.nextContactAt").value("2026-07-28T10:00:00"))
                .andExpect(jsonPath("$.notes").value("Waiting for response"))
                .andExpect(jsonPath("$.createdAt").value("2026-07-21T10:05:00"))
                .andExpect(jsonPath("$.updatedAt").value("2026-07-21T10:05:00"));

        verify(applicationService).getApplicationById(applicationId);
        verify(applicationMapper).toResponse(application);
    }

    @Test
    void shouldReturnNotFoundWhenGettingMissingApplication()
            throws Exception {

        Long applicationId = 999L;

        ResourceNotFoundException exception =
                new ResourceNotFoundException(
                        "Application not found with id: " + applicationId
                );

        when(applicationService.getApplicationById(applicationId))
                .thenThrow(exception);

        mockMvc.perform(get("/api/applications/{id}", applicationId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message")
                        .value("Application not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/applications/999"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).getApplicationById(applicationId);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldUpdateApplication() throws Exception {
        Long applicationId = 30L;

        ApplicationUpdateRequest request =
                new ApplicationUpdateRequest(
                        20L,
                        LocalDateTime.of(2026, 7, 2, 11, 0),
                        LocalDateTime.of(2026, 7, 10, 12, 0),
                        "Updated notes"
                );

        Application updatedApplication = mock(Application.class);

        ApplicationResponse response = new ApplicationResponse(
                30L,
                20L,
                "Java Backend Intern",
                5L,
                "Example Company",
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 2, 11, 0),
                LocalDateTime.of(2026, 7, 10, 12, 0),
                "Updated notes",
                LocalDateTime.of(2026, 7, 1, 10, 0),
                LocalDateTime.of(2026, 7, 2, 11, 5)
        );

        when(applicationService.updateApplication(applicationId, request))
                .thenReturn(updatedApplication);
        when(applicationMapper.toResponse(updatedApplication))
                .thenReturn(response);

        mockMvc.perform(put("/api/applications/{id}", applicationId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.vacancyId").value(20L))
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.appliedAt").value("2026-07-02T11:00:00"))
                .andExpect(jsonPath("$.nextContactAt").value("2026-07-10T12:00:00"))
                .andExpect(jsonPath("$.notes").value("Updated notes"));

        verify(applicationService).updateApplication(applicationId, request);
        verify(applicationMapper).toResponse(updatedApplication);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingApplication()
            throws Exception {

        Long applicationId = 999L;

        ApplicationUpdateRequest request =
                new ApplicationUpdateRequest(
                        20L,
                        null,
                        null,
                        "Updated notes"
                );

        ResourceNotFoundException exception =
                new ResourceNotFoundException(
                        "Application not found with id: " + applicationId
                );

        when(applicationService.updateApplication(applicationId, request))
                .thenThrow(exception);

        mockMvc.perform(put("/api/applications/{id}", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message")
                        .value("Application not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/applications/999"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).updateApplication(applicationId, request);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldUpdateApplicationStatus() throws Exception {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.INTERVIEW
                );

        Application updatedApplication = mock(Application.class);

        ApplicationResponse response = new ApplicationResponse(
                30L,
                20L,
                "Java Backend Intern",
                5L,
                "Example Company",
                ApplicationStatus.INTERVIEW,
                LocalDateTime.of(2026, 7, 2, 11, 0),
                LocalDateTime.of(2026, 7, 10, 12, 0),
                "Updated notes",
                LocalDateTime.of(2026, 7, 1, 10, 0),
                LocalDateTime.of(2026, 7, 2, 11, 5)
        );

        when(applicationService.updateApplicationStatus(applicationId, request))
                .thenReturn(updatedApplication);
        when(applicationMapper.toResponse(updatedApplication))
                .thenReturn(response);

        mockMvc.perform(patch("/api/applications/{id}/status", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.status").value("INTERVIEW"));

        verify(applicationService).updateApplicationStatus(applicationId, request);
        verify(applicationMapper).toResponse(updatedApplication);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingStatusOfMissingApplication()
            throws Exception {

        Long applicationId = 999L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.INTERVIEW
                );

        ResourceNotFoundException exception =
                new ResourceNotFoundException(
                        "Application not found with id: " + applicationId
                );

        when(applicationService.updateApplicationStatus(applicationId, request))
                .thenThrow(exception);

        mockMvc.perform(patch("/api/applications/{id}/status", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message")
                        .value("Application not found with id: 999"))
                .andExpect(jsonPath("$.path")
                        .value("/api/applications/999/status"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).updateApplicationStatus(applicationId, request);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsMissing() throws Exception {
        Long applicationId = 30L;

        String requestBody = """
                {
                  "status": null
                }
                """;

        mockMvc.perform(patch(
                        "/api/applications/{id}/status",
                        applicationId
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.status")
                        .value("Status is required"));

        verifyNoInteractions(applicationService, applicationMapper);
    }

    @Test
    void shouldDeleteApplication() throws Exception {
        Long applicationId = 30L;

        mockMvc.perform(delete("/api/applications/{id}", applicationId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(applicationService).deleteApplication(applicationId);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMissingApplication() throws Exception {
        Long applicationId = 999L;

        ResourceNotFoundException exception = new ResourceNotFoundException(
                "Application not found with id: 999"
        );

        doThrow(exception)
                .when(applicationService)
                .deleteApplication(applicationId);

        mockMvc.perform(delete("/api/applications/{id}", applicationId))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message").value("Application not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/applications/999"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).deleteApplication(applicationId);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnBadRequestWhenDatesAreInvalidOnCreate() throws Exception {
        ApplicationRequest request = new ApplicationRequest(
                20L,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 20, 10, 0),
                LocalDateTime.of(2026, 7, 19, 10, 0),
                "Notes"
        );

        InvalidApplicationDataException exception =
                new InvalidApplicationDataException(
                        "Next contact date must not be before applied date"
                );

        when(applicationService.createApplication(request))
                .thenThrow(exception);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Next contact date must not be before applied date"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).createApplication(request);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnBadRequestWhenDatesAreInvalidOnUpdate() throws Exception {
        Long applicationId = 30L;

        ApplicationUpdateRequest request = new ApplicationUpdateRequest(
                20L,
                LocalDateTime.of(2026, 7, 20, 10, 0),
                LocalDateTime.of(2026, 7, 19, 10, 0),
                "Updated notes"
        );

        InvalidApplicationDataException exception =
                new InvalidApplicationDataException(
                        "Next contact date must not be before applied date"
                );

        when(applicationService.updateApplication(applicationId, request))
                .thenThrow(exception);

        mockMvc.perform(put("/api/applications/{id}", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Next contact date must not be before applied date"))
                .andExpect(jsonPath("$.path").value("/api/applications/30"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).updateApplication(applicationId, request);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnBadRequestWhenChangingTerminalStatus() throws Exception {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.INTERVIEW
                );

        InvalidApplicationDataException exception =
                new InvalidApplicationDataException(
                        "Cannot change status from REJECTED to INTERVIEW"
                );

        when(applicationService.updateApplicationStatus(applicationId, request))
                .thenThrow(exception);

        mockMvc.perform(patch("/api/applications/{id}/status", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Cannot change status from REJECTED to INTERVIEW"))
                .andExpect(jsonPath("$.path")
                        .value("/api/applications/30/status"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).updateApplicationStatus(applicationId, request);
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnCurrentApplicationWhenStatusDoesNotChange() throws Exception {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.REJECTED
                );

        Application application = mock(Application.class);

        ApplicationResponse response = new ApplicationResponse(
                30L,
                20L,
                "Java Backend Intern",
                5L,
                "Example Company",
                ApplicationStatus.REJECTED,
                LocalDateTime.of(2026, 7, 2, 11, 0),
                LocalDateTime.of(2026, 7, 10, 12, 0),
                "Notes",
                LocalDateTime.of(2026, 7, 1, 10, 0),
                LocalDateTime.of(2026, 7, 2, 11, 5)
        );

        when(applicationService.updateApplicationStatus(applicationId, request))
                .thenReturn(application);
        when(applicationMapper.toResponse(application))
                .thenReturn(response);

        mockMvc.perform(patch("/api/applications/{id}/status", applicationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(30L))
                .andExpect(jsonPath("$.status").value("REJECTED"));

        verify(applicationService).updateApplicationStatus(applicationId, request);
        verify(applicationMapper).toResponse(application);
    }

    @Test
    void shouldUseDefaultPaginationParameters() throws Exception {
        int defaultPage = 0;
        int defaultSize = 10;
        String defaultSortBy = "createdAt";
        String defaultDirection = "DESC";
        long totalElements = 0;
        int totalPages = 0;

        List<ApplicationResponse> content = List.of();

        PagedResponse<ApplicationResponse> pagedResponse =
                new PagedResponse<>(
                        content,
                        defaultPage,
                        defaultSize,
                        totalElements,
                        totalPages
                );

        when(applicationService.getAllApplications(
                defaultPage,
                defaultSize,
                defaultSortBy,
                defaultDirection,
                null,
                null
        )).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/applications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.page").value(defaultPage))
                .andExpect(jsonPath("$.size").value(defaultSize))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages));

        verify(applicationService).getAllApplications(
                defaultPage,
                defaultSize,
                defaultSortBy,
                defaultDirection,
                null,
                null
        );
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldRejectNegativePage() throws Exception {
        int invalidPage = -1;
        int validSize = 10;

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(invalidPage))
                        .param("size", String.valueOf(validSize)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("must be greater than or equal to 0"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verifyNoInteractions(applicationService, applicationMapper);
    }

    @Test
    void shouldRejectZeroPageSize() throws Exception {
        int validPage = 0;
        int invalidSize = 0;

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(validPage))
                        .param("size", String.valueOf(invalidSize)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("must be greater than or equal to 1"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verifyNoInteractions(applicationService, applicationMapper);
    }

    @Test
    void shouldRejectPageSizeAboveMaximum() throws Exception {
        int validPage = 0;
        int invalidSize = 101;

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(validPage))
                        .param("size", String.valueOf(invalidSize)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("must be less than or equal to 100"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verifyNoInteractions(applicationService, applicationMapper);
    }

    @Test
    void shouldRejectUnsupportedSortField() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "unknownField";
        String direction = "ASC";

        InvalidApplicationDataException exception =
                new InvalidApplicationDataException(
                        "Unsupported sort field: " + sortBy
                );

        when(applicationService.getAllApplications(
                page,
                size,
                sortBy,
                direction,
                null,
                null
        )).thenThrow(exception);

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Unsupported sort field: unknownField"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).getAllApplications(
                page,
                size,
                sortBy,
                direction,
                null,
                null
        );
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldRejectUnsupportedSortDirection() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "SIDEWAYS";

        InvalidApplicationDataException exception =
                new InvalidApplicationDataException(
                        "Unsupported sort direction: " + direction
                );

        when(applicationService.getAllApplications(
                page,
                size,
                sortBy,
                direction,
                null,
                null
        )).thenThrow(exception);

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Unsupported sort direction: SIDEWAYS"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(applicationService).getAllApplications(
                page,
                size,
                sortBy,
                direction,
                null,
                null
        );
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldGetApplicationsFilteredByStatus() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "DESC";
        ApplicationStatus status = ApplicationStatus.INTERVIEW;

        long totalElements = 1;
        int totalPages = 1;

        ApplicationResponse response = new ApplicationResponse(
                30L,
                20L,
                "Java Backend Intern",
                5L,
                "Example Company",
                ApplicationStatus.INTERVIEW,
                LocalDateTime.of(2026, 7, 20, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Technical interview scheduled",
                LocalDateTime.of(2026, 7, 20, 10, 5),
                LocalDateTime.of(2026, 7, 22, 12, 0)
        );

        List<ApplicationResponse> content = List.of(response);

        PagedResponse<ApplicationResponse> pagedResponse =
                new PagedResponse<>(
                        content,
                        page,
                        size,
                        totalElements,
                        totalPages
                );

        when(applicationService.getAllApplications(
                page,
                size,
                sortBy,
                direction,
                status,
                null
        )).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction)
                        .param("status", status.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(response.id()))
                .andExpect(jsonPath("$.content[0].status").value(status.name()))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages));

        verify(applicationService).getAllApplications(
                page,
                size,
                sortBy,
                direction,
                status,
                null
        );
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldRejectUnsupportedApplicationStatus() throws Exception {
        String status = "UNKNOWN";

        mockMvc.perform(get("/api/applications")
                        .param("status", status))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad request"))
                .andExpect(jsonPath("$.message")
                        .value("Unsupported value for parameter status: UNKNOWN"))
                .andExpect(jsonPath("$.path").value("/api/applications"))
                .andExpect(jsonPath("$.fieldErrors").isMap())
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verifyNoInteractions(applicationService, applicationMapper);
    }

    @Test
    void shouldGetApplicationsFilteredByVacancyId() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "DESC";
        ApplicationStatus status = null;
        Long vacancyId = 20L;
        long totalElements = 1;
        int totalPages = 1;

        ApplicationResponse response = new ApplicationResponse(
                30L,
                vacancyId,
                "Java Backend Intern",
                5L,
                "Example Company",
                ApplicationStatus.INTERVIEW,
                LocalDateTime.of(2026, 7, 20, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Technical interview scheduled",
                LocalDateTime.of(2026, 7, 20, 10, 5),
                LocalDateTime.of(2026, 7, 22, 12, 0)
        );

        List<ApplicationResponse> content = List.of(response);

        PagedResponse<ApplicationResponse> pagedResponse =
                new PagedResponse<>(
                        content,
                        page,
                        size,
                        totalElements,
                        totalPages
                );

        when(applicationService.getAllApplications(
                page,
                size,
                sortBy,
                direction,
                status,
                vacancyId
        )).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction)
                        .param("vacancyId", String.valueOf(vacancyId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].vacancyId").value(vacancyId))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(response.id()))
                .andExpect(jsonPath("$.content[0].status").value(response.status().name()))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages));

        verify(applicationService).getAllApplications(
                page,
                size,
                sortBy,
                direction,
                status,
                vacancyId
        );
        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldGetApplicationsFilteredByStatusAndVacancyId() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "DESC";
        ApplicationStatus status = ApplicationStatus.INTERVIEW;
        Long vacancyId = 20L;
        long totalElements = 1;
        int totalPages = 1;

        ApplicationResponse response = new ApplicationResponse(
                30L,
                vacancyId,
                "Java Backend Intern",
                5L,
                "Example Company",
                status,
                LocalDateTime.of(2026, 7, 20, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Technical interview scheduled",
                LocalDateTime.of(2026, 7, 20, 10, 5),
                LocalDateTime.of(2026, 7, 22, 12, 0)
        );

        List<ApplicationResponse> content = List.of(response);

        PagedResponse<ApplicationResponse> pagedResponse =
                new PagedResponse<>(
                        content,
                        page,
                        size,
                        totalElements,
                        totalPages
                );

        when(applicationService.getAllApplications(
                page,
                size,
                sortBy,
                direction,
                status,
                vacancyId
        )).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/applications")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction)
                        .param("status", status.name())
                        .param("vacancyId", String.valueOf(vacancyId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].vacancyId").value(vacancyId))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(response.id()))
                .andExpect(jsonPath("$.content[0].status").value(status.name()))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages));

        verify(applicationService).getAllApplications(
                page,
                size,
                sortBy,
                direction,
                status,
                vacancyId
        );
        verifyNoInteractions(applicationMapper);
    }
}
