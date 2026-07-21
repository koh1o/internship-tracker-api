package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationUpdateRequest;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.ApplicationMapper;
import io.github.koh1o.internshiptrackerapi.repository.ApplicationRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void shouldGetAllApplications() {
        Application firstApplication = mock(Application.class);
        Application secondApplication = mock(Application.class);

        List<Application> applications = List.of(
                firstApplication,
                secondApplication
        );

        when(applicationRepository.findAll())
                .thenReturn(applications);

        List<Application> result = applicationService.getAllApplications();

        assertSame(applications, result);

        verify(applicationRepository).findAll();
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

        Application application = mock(Application.class);

        ApplicationUpdateRequest request =
                new ApplicationUpdateRequest(
                        vacancyId,
                        null,
                        null,
                        "Updated notes"
                );

        when(applicationRepository.findById(applicationId))
                .thenReturn(Optional.of(application));
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
}
