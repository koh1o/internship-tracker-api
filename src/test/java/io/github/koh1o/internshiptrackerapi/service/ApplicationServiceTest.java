package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.dto.PagedResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationStatusUpdateRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationUpdateRequest;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.exception.InvalidApplicationDataException;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.ApplicationMapper;
import io.github.koh1o.internshiptrackerapi.repository.ApplicationRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ApplicationServiceTest {

    private final ApplicationRepository applicationRepository =
            mock(ApplicationRepository.class);

    private final VacancyRepository vacancyRepository =
            mock(VacancyRepository.class);

    private final ApplicationMapper applicationMapper =
            mock(ApplicationMapper.class);

    private final ApplicationService applicationService =
            new ApplicationService(
                    applicationRepository,
                    vacancyRepository,
                    applicationMapper
            );

    @Test
    void shouldCreateApplication() {
        Long vacancyId = 20L;

        ApplicationRequest request = new ApplicationRequest(
                vacancyId,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Waiting for response"
        );

        Vacancy vacancy = mock(Vacancy.class);
        Application application = mock(Application.class);
        Application savedApplication = mock(Application.class);

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.of(vacancy));

        when(applicationMapper.toEntity(request, vacancy))
                .thenReturn(application);

        when(applicationRepository.save(application))
                .thenReturn(savedApplication);

        Application result = applicationService.createApplication(request);

        assertSame(savedApplication, result);

        verify(vacancyRepository).findById(vacancyId);
        verify(applicationMapper).toEntity(request, vacancy);
        verify(applicationRepository).save(application);
    }

    @Test
    void shouldThrowExceptionWhenCreatingApplicationForMissingVacancy() {
        Long vacancyId = 999L;

        ApplicationRequest request = new ApplicationRequest(
                vacancyId,
                ApplicationStatus.APPLIED,
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 28, 10, 0),
                "Waiting for response"
        );

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.createApplication(request)
        );

        assertEquals("Vacancy not found with id: 999", exception.getMessage());

        verify(vacancyRepository).findById(vacancyId);
        verifyNoInteractions(applicationMapper, applicationRepository);
    }

    @Test
    void shouldGetApplicationById() {
        Long applicationId = 30L;
        Application application = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));

        Application result = applicationService.getApplicationById(applicationId);

        assertSame(application, result);

        verify(applicationRepository).findById(applicationId);
    }

    @Test
    void shouldThrowExceptionWhenGettingMissingApplication() {
        Long applicationId = 999L;

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.getApplicationById(applicationId)
        );

        assertEquals("Application not found with id: 999", exception.getMessage());

        verify(applicationRepository).findById(applicationId);
    }

    @Test
    void shouldUpdateApplication() {
        Long applicationId = 30L;
        Long vacancyId = 20L;

        Application application = mock(Application.class);
        Vacancy vacancy = mock(Vacancy.class);
        Application savedApplication = mock(Application.class);

        ApplicationUpdateRequest request =
                new ApplicationUpdateRequest(
                        vacancyId,
                        LocalDateTime.of(2026, 7, 2, 11, 0),
                        LocalDateTime.of(2026, 7, 10, 12, 0),
                        "Updated notes"
                );

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.of(vacancy));
        when(applicationRepository.save(application))
                .thenReturn(savedApplication);

        Application result = applicationService.updateApplication(applicationId, request);

        assertSame(savedApplication, result);

        verify(applicationRepository).findById(applicationId);
        verify(vacancyRepository).findById(vacancyId);
        verify(applicationMapper).updateEntity(application, request, vacancy);
        verify(applicationRepository).save(application);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingApplication() {
        Long applicationId = 999L;

        ApplicationUpdateRequest request =
                new ApplicationUpdateRequest(
                        20L,
                        null,
                        null,
                        "Updated notes"
                );

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.updateApplication(applicationId, request)
        );

        assertEquals("Application not found with id: 999", exception.getMessage());

        verify(applicationRepository).findById(applicationId);
        verifyNoInteractions(applicationMapper, vacancyRepository);
        verify(applicationRepository, never())
                .save(any(Application.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithMissingVacancy() {
        Long applicationId = 30L;
        Long vacancyId = 888L;
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 7, 20, 10, 0);

        Application application = mock(Application.class);

        ApplicationUpdateRequest request =
                new ApplicationUpdateRequest(
                        vacancyId,
                        appliedAt,
                        null,
                        "Updated notes"
                );

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));

        when(application.getStatus())
                .thenReturn(ApplicationStatus.APPLIED);

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.updateApplication(applicationId, request)
        );

        assertEquals(
                "Vacancy not found with id: 888",
                exception.getMessage()
        );

        verify(applicationRepository).findById(applicationId);
        verify(vacancyRepository).findById(vacancyId);
        verifyNoInteractions(applicationMapper);
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void shouldUpdateApplicationStatus() {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.INTERVIEW
                );

        Application application = mock(Application.class);
        Application savedApplication = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(application))
                .thenReturn(savedApplication);
        when(application.getAppliedAt())
                .thenReturn(LocalDateTime.of(2026, 7, 20, 10, 0));
        when(application.getStatus())
                .thenReturn(ApplicationStatus.APPLIED);

        Application result = applicationService.updateApplicationStatus(applicationId, request);

        assertSame(savedApplication, result);

        verify(application).getStatus();
        verify(applicationRepository).findById(applicationId);
        verify(application).setStatus(ApplicationStatus.INTERVIEW);
        verify(applicationRepository).save(application);
        verify(application).getAppliedAt();
    }

    @Test
    void shouldThrowExceptionWhenUpdatingStatusOfMissingApplication() {
        Long applicationId = 999L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.INTERVIEW
                );

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.updateApplicationStatus(applicationId, request)
        );

        assertEquals("Application not found with id: 999", exception.getMessage());

        verify(applicationRepository).findById(applicationId);
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void shouldDeleteApplication() {
        Long applicationId = 30L;
        Application application = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));

        applicationService.deleteApplication(applicationId);

        verify(applicationRepository).findById(applicationId);
        verify(applicationRepository).delete(application);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingApplication() {
        Long applicationId = 999L;

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.deleteApplication(applicationId)
        );

        assertEquals("Application not found with id: 999", exception.getMessage());

        verify(applicationRepository).findById(applicationId);
        verify(applicationRepository, never()).delete(any(Application.class));
    }

    @Test
    void shouldThrowExceptionWhenNextContactAtIsBeforeAppliedAtOnCreate() {
        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 7, 20, 10, 0);
        LocalDateTime nextContactAt =
                LocalDateTime.of(2026, 7, 19, 10, 0);

        ApplicationRequest request = new ApplicationRequest(
                20L,
                ApplicationStatus.APPLIED,
                appliedAt,
                nextContactAt,
                "Notes"
        );

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.createApplication(request)
        );

        assertEquals(
                "Next contact date must not be before applied date",
                exception.getMessage()
        );

        verifyNoInteractions(
                vacancyRepository,
                applicationRepository,
                applicationMapper
        );
    }

    @Test
    void shouldThrowExceptionWhenNextContactAtIsBeforeAppliedAtOnUpdate() {
        Long applicationId = 30L;

        LocalDateTime appliedAt =
                LocalDateTime.of(2026, 7, 20, 10, 0);
        LocalDateTime nextContactAt =
                LocalDateTime.of(2026, 7, 19, 10, 0);

        ApplicationUpdateRequest request = new ApplicationUpdateRequest(
                20L,
                appliedAt,
                nextContactAt,
                "Updated notes"
        );

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.updateApplication(applicationId, request)
        );

        assertEquals(
                "Next contact date must not be before applied date",
                exception.getMessage()
        );

        verifyNoInteractions(
                vacancyRepository,
                applicationRepository,
                applicationMapper
        );
    }

    @Test
    void shouldThrowExceptionWhenStatusTransitionIsNotAllowed() {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.INTERVIEW
                );

        Application application = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(application.getStatus())
                .thenReturn(ApplicationStatus.REJECTED);

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.updateApplicationStatus(applicationId, request)
        );

        assertEquals(
                "Cannot change status from REJECTED to INTERVIEW",
                exception.getMessage()
        );

        verify(applicationRepository).findById(applicationId);
        verify(application).getStatus();
        verify(application, never())
                .setStatus(any(ApplicationStatus.class));
        verify(applicationRepository, never())
                .save(any(Application.class));
    }

    @Test
    void shouldReturnApplicationWithoutSavingWhenStatusDoesNotChange() {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.REJECTED
                );

        Application application = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(application.getStatus())
                .thenReturn(ApplicationStatus.REJECTED);

        Application result = applicationService.updateApplicationStatus(applicationId, request);

        assertSame(application, result);

        verify(applicationRepository).findById(applicationId);
        verify(application).getStatus();
        verify(application, never()).setStatus(any(ApplicationStatus.class));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void shouldThrowExceptionWhenTransitionFromPlannedToInterview() {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.INTERVIEW
                );

        Application application = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(application.getStatus())
                .thenReturn(ApplicationStatus.PLANNED);

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.updateApplicationStatus(applicationId, request)
        );

        assertEquals(
                "Cannot change status from PLANNED to INTERVIEW",
                exception.getMessage()
        );

        verify(applicationRepository).findById(applicationId);
        verify(application).getStatus();
        verify(application, never())
                .setStatus(any(ApplicationStatus.class));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void shouldUpdateStatusFromAppliedToTestTask() {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.TEST_TASK
                );

        Application application = mock(Application.class);
        Application savedApplication = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(application.getAppliedAt())
                .thenReturn(LocalDateTime.of(2026, 7, 20, 10, 0));
        when(application.getStatus())
                .thenReturn(ApplicationStatus.APPLIED);
        when(applicationRepository.save(application))
                .thenReturn(savedApplication);

        Application result = applicationService.updateApplicationStatus(applicationId, request);

        assertSame(savedApplication, result);

        verify(applicationRepository).findById(applicationId);
        verify(application).getStatus();
        verify(application).setStatus(ApplicationStatus.TEST_TASK);
        verify(applicationRepository).save(application);
        verify(application).getAppliedAt();
    }

    @Test
    void shouldThrowExceptionWhenAppliedAtIsMissingForAppliedStatusOnCreate() {
        ApplicationRequest request = new ApplicationRequest(
                20L,
                ApplicationStatus.APPLIED,
                null,
                null,
                "Notes"
        );

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.createApplication(request)
        );

        assertEquals(
                "Applied date is required for status APPLIED",
                exception.getMessage()
        );

        verifyNoInteractions(
                vacancyRepository,
                applicationRepository,
                applicationMapper
        );
    }

    @Test
    void shouldThrowExceptionWhenAppliedAtIsRemovedFromAppliedApplication() {
        Long applicationId = 30L;

        ApplicationUpdateRequest request = new ApplicationUpdateRequest(
                20L,
                null,
                null,
                "Updated notes"
        );

        Application application = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(application.getStatus())
                .thenReturn(ApplicationStatus.APPLIED);

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.updateApplication(applicationId, request)
        );

        assertEquals(
                "Applied date is required for status APPLIED",
                exception.getMessage()
        );

        verifyNoInteractions(vacancyRepository, applicationMapper);
        verify(applicationRepository).findById(applicationId);
        verify(application).getStatus();
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void shouldThrowExceptionWhenChangingToAppliedWithoutAppliedAt() {
        Long applicationId = 30L;

        ApplicationStatusUpdateRequest request =
                new ApplicationStatusUpdateRequest(
                        ApplicationStatus.APPLIED
                );

        Application application = mock(Application.class);

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
        when(application.getStatus())
                .thenReturn(ApplicationStatus.PLANNED);
        when(application.getAppliedAt())
                .thenReturn(null);

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.updateApplicationStatus(applicationId, request)
        );

        assertEquals(
                "Applied date is required for status APPLIED",
                exception.getMessage()
        );

        verify(applicationRepository).findById(applicationId);
        verify(application).getStatus();
        verify(application).getAppliedAt();

        verify(application, never())
                .setStatus(any(ApplicationStatus.class));

        verify(applicationRepository, never())
                .save(any(Application.class));
    }

    @Test
    void shouldReturnRequestedApplicationPage() {
        int page = 0;
        int size = 2;

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                "createdAt"
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Application firstApplication =
                mock(Application.class);

        Application secondApplication =
                mock(Application.class);

        ApplicationResponse firstResponse =
                mock(ApplicationResponse.class);

        ApplicationResponse secondResponse =
                mock(ApplicationResponse.class);

        List<Application> applications = List.of(
                firstApplication,
                secondApplication
        );

        List<ApplicationResponse> expectedContent = List.of(
                firstResponse,
                secondResponse
        );

        Page<Application> applicationPage =
                new PageImpl<>(
                        applications,
                        pageable,
                        5
                );

        when(applicationRepository.findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(applicationPage);

        when(applicationMapper.toResponse(firstApplication))
                .thenReturn(firstResponse);

        when(applicationMapper.toResponse(secondApplication))
                .thenReturn(secondResponse);

        PagedResponse<ApplicationResponse> result =
                applicationService.getAllApplications(
                        page,
                        size
                );

        assertEquals(expectedContent, result.content());
        assertEquals(applicationPage.getNumber(), result.page());
        assertEquals(applicationPage.getSize(), result.size());
        assertEquals(
                applicationPage.getTotalElements(),
                result.totalElements()
        );
        assertEquals(
                applicationPage.getTotalPages(),
                result.totalPages()
        );

        verify(applicationRepository).findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        );

        verify(applicationMapper).toResponse(firstApplication);
        verify(applicationMapper).toResponse(secondApplication);
    }

    @Test
    void shouldReturnEmptyApplicationPage() {
        int page = 5;
        int size = 10;

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                "createdAt"
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Page<Application> applicationPage =
                Page.empty(pageable);

        when(applicationRepository.findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(applicationPage);

        PagedResponse<ApplicationResponse> result =
                applicationService.getAllApplications(
                        page,
                        size
                );

        assertTrue(result.content().isEmpty());
        assertEquals(applicationPage.getNumber(), result.page());
        assertEquals(applicationPage.getSize(), result.size());
        assertEquals(
                applicationPage.getTotalElements(),
                result.totalElements()
        );
        assertEquals(
                applicationPage.getTotalPages(),
                result.totalPages()
        );

        verify(applicationRepository).findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        );

        verifyNoInteractions(applicationMapper);
    }

    @Test
    void shouldReturnApplicationsSortedDescendingByCreatedAt() {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "DESC";
        long totalElements = 2;

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                sortBy
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Application firstApplication =
                mock(Application.class);

        Application secondApplication =
                mock(Application.class);

        ApplicationResponse firstResponse =
                mock(ApplicationResponse.class);

        ApplicationResponse secondResponse =
                mock(ApplicationResponse.class);

        List<Application> applications = List.of(
                firstApplication,
                secondApplication
        );

        List<ApplicationResponse> expectedContent = List.of(
                firstResponse,
                secondResponse
        );

        Page<Application> applicationPage =
                new PageImpl<>(
                        applications,
                        pageable,
                        totalElements
                );

        when(applicationRepository.findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(applicationPage);

        when(applicationMapper.toResponse(firstApplication))
                .thenReturn(firstResponse);

        when(applicationMapper.toResponse(secondApplication))
                .thenReturn(secondResponse);

        PagedResponse<ApplicationResponse> result =
                applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        direction
                );

        assertEquals(expectedContent, result.content());
        assertEquals(applicationPage.getNumber(), result.page());
        assertEquals(applicationPage.getSize(), result.size());
        assertEquals(
                applicationPage.getTotalElements(),
                result.totalElements()
        );
        assertEquals(
                applicationPage.getTotalPages(),
                result.totalPages()
        );

        verify(applicationRepository).findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        );

        verify(applicationMapper).toResponse(firstApplication);
        verify(applicationMapper).toResponse(secondApplication);
    }

    @Test
    void shouldReturnApplicationsSortedAscendingByAppliedAt() {
        int page = 1;
        int size = 5;
        String sortBy = "appliedAt";
        String direction = "ASC";
        long totalElements = 8;

        Sort sort = Sort.by(
                Sort.Direction.ASC,
                sortBy
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Application firstApplication =
                mock(Application.class);

        Application secondApplication =
                mock(Application.class);

        ApplicationResponse firstResponse =
                mock(ApplicationResponse.class);

        ApplicationResponse secondResponse =
                mock(ApplicationResponse.class);

        List<Application> applications = List.of(
                firstApplication,
                secondApplication
        );

        List<ApplicationResponse> expectedContent = List.of(
                firstResponse,
                secondResponse
        );

        Page<Application> applicationPage =
                new PageImpl<>(
                        applications,
                        pageable,
                        totalElements
                );

        when(applicationRepository.findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(applicationPage);

        when(applicationMapper.toResponse(firstApplication))
                .thenReturn(firstResponse);

        when(applicationMapper.toResponse(secondApplication))
                .thenReturn(secondResponse);

        PagedResponse<ApplicationResponse> result =
                applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        direction
                );

        assertEquals(expectedContent, result.content());
        assertEquals(applicationPage.getNumber(), result.page());
        assertEquals(applicationPage.getSize(), result.size());
        assertEquals(
                applicationPage.getTotalElements(),
                result.totalElements()
        );
        assertEquals(
                applicationPage.getTotalPages(),
                result.totalPages()
        );

        verify(applicationRepository).findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        );

        verify(applicationMapper).toResponse(firstApplication);
        verify(applicationMapper).toResponse(secondApplication);
    }

    @Test
    void shouldRejectUnsupportedSortField() {
        int page = 0;
        int size = 10;
        String sortBy = "unknownField";
        String direction = "ASC";

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );

        assertEquals("Unsupported sort field: unknownField", exception.getMessage());

        verifyNoInteractions(applicationRepository, applicationMapper);
    }

    @Test
    void shouldRejectUnsupportedSortDirection() {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "SIDEWAYS";

        InvalidApplicationDataException exception = assertThrows(
                InvalidApplicationDataException.class,
                () -> applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );

        assertEquals(
                "Unsupported sort direction: SIDEWAYS",
                exception.getMessage()
        );

        verifyNoInteractions(applicationRepository, applicationMapper);
    }

    @Test
    void shouldReturnApplicationsFilteredByStatus() {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "DESC";
        ApplicationStatus status =
                ApplicationStatus.INTERVIEW;
        long totalElements = 2;

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                sortBy
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Application firstApplication =
                mock(Application.class);

        Application secondApplication =
                mock(Application.class);

        ApplicationResponse firstResponse =
                mock(ApplicationResponse.class);

        ApplicationResponse secondResponse =
                mock(ApplicationResponse.class);

        List<Application> applications = List.of(
                firstApplication,
                secondApplication
        );

        List<ApplicationResponse> expectedContent = List.of(
                firstResponse,
                secondResponse
        );

        Page<Application> applicationPage =
                new PageImpl<>(
                        applications,
                        pageable,
                        totalElements
                );

        when(applicationRepository.findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(applicationPage);

        when(applicationMapper.toResponse(firstApplication))
                .thenReturn(firstResponse);

        when(applicationMapper.toResponse(secondApplication))
                .thenReturn(secondResponse);

        PagedResponse<ApplicationResponse> result =
                applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        direction,
                        status
                );

        assertEquals(expectedContent, result.content());
        assertEquals(applicationPage.getNumber(), result.page());
        assertEquals(applicationPage.getSize(), result.size());
        assertEquals(
                applicationPage.getTotalElements(),
                result.totalElements()
        );
        assertEquals(
                applicationPage.getTotalPages(),
                result.totalPages()
        );

        verify(applicationRepository).findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        );

        verify(applicationMapper).toResponse(firstApplication);
        verify(applicationMapper).toResponse(secondApplication);
    }

    @Test
    void shouldReturnApplicationsFilteredByVacancyId() {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "DESC";
        Long vacancyId = 20L;
        long totalElements = 2;

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                sortBy
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Application firstApplication =
                mock(Application.class);

        Application secondApplication =
                mock(Application.class);

        ApplicationResponse firstResponse =
                mock(ApplicationResponse.class);

        ApplicationResponse secondResponse =
                mock(ApplicationResponse.class);

        List<Application> applications = List.of(
                firstApplication,
                secondApplication
        );

        List<ApplicationResponse> expectedContent = List.of(
                firstResponse,
                secondResponse
        );

        Page<Application> applicationPage =
                new PageImpl<>(
                        applications,
                        pageable,
                        totalElements
                );

        when(applicationRepository.findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(applicationPage);

        when(applicationMapper.toResponse(firstApplication))
                .thenReturn(firstResponse);

        when(applicationMapper.toResponse(secondApplication))
                .thenReturn(secondResponse);

        PagedResponse<ApplicationResponse> result =
                applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        direction,
                        null,
                        vacancyId
                );

        assertEquals(expectedContent, result.content());
        assertEquals(applicationPage.getNumber(), result.page());
        assertEquals(applicationPage.getSize(), result.size());
        assertEquals(
                applicationPage.getTotalElements(),
                result.totalElements()
        );
        assertEquals(
                applicationPage.getTotalPages(),
                result.totalPages()
        );

        verify(applicationRepository).findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        );

        verify(applicationMapper).toResponse(firstApplication);
        verify(applicationMapper).toResponse(secondApplication);
    }

    @Test
    void shouldReturnApplicationsFilteredByStatusAndVacancyId() {
        int page = 0;
        int size = 10;
        String sortBy = "createdAt";
        String direction = "DESC";
        ApplicationStatus status =
                ApplicationStatus.INTERVIEW;
        Long vacancyId = 20L;
        long totalElements = 1;

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                sortBy
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Application application =
                mock(Application.class);

        ApplicationResponse response =
                mock(ApplicationResponse.class);

        List<Application> applications =
                List.of(application);

        List<ApplicationResponse> expectedContent =
                List.of(response);

        Page<Application> applicationPage =
                new PageImpl<>(
                        applications,
                        pageable,
                        totalElements
                );

        when(applicationRepository.findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        )).thenReturn(applicationPage);

        when(applicationMapper.toResponse(application))
                .thenReturn(response);

        PagedResponse<ApplicationResponse> result =
                applicationService.getAllApplications(
                        page,
                        size,
                        sortBy,
                        direction,
                        status,
                        vacancyId
                );

        assertEquals(expectedContent, result.content());
        assertEquals(applicationPage.getNumber(), result.page());
        assertEquals(applicationPage.getSize(), result.size());
        assertEquals(
                applicationPage.getTotalElements(),
                result.totalElements()
        );
        assertEquals(
                applicationPage.getTotalPages(),
                result.totalPages()
        );

        verify(applicationRepository).findAll(
                ArgumentMatchers.<Specification<Application>>any(),
                ArgumentMatchers.eq(pageable)
        );

        verify(applicationMapper).toResponse(application);
    }
}
