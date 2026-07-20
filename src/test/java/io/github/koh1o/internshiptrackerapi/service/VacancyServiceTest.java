package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyRequest;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.entity.WorkFormat;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.VacancyMapper;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class VacancyServiceTest {

    private VacancyRepository vacancyRepository;
    private CompanyRepository companyRepository;
    private VacancyMapper vacancyMapper;
    private VacancyService vacancyService;

    @BeforeEach
    void setUp() {
        vacancyRepository = mock(VacancyRepository.class);
        companyRepository = mock(CompanyRepository.class);
        vacancyMapper = mock(VacancyMapper.class);

        vacancyService = new VacancyService(
                vacancyRepository,
                companyRepository,
                vacancyMapper
        );
    }

    @Test
    void shouldCreateVacancy() {
        Long companyId = 1L;

        Company company = new Company(
                "Example Company",
                "https://example.com",
                "Description"
        );

        VacancyRequest request = new VacancyRequest(
                companyId,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description"
        );

        Vacancy vacancy = mock(Vacancy.class);
        Vacancy savedVacancy = mock(Vacancy.class);

        when(companyRepository.findById(companyId))
                .thenReturn(Optional.of(company));

        when(vacancyMapper.toEntity(request, company))
                .thenReturn(vacancy);

        when(vacancyRepository.save(vacancy))
                .thenReturn(savedVacancy);

        Vacancy result = vacancyService.createVacancy(request);
        assertSame(savedVacancy, result);

        verify(companyRepository).findById(companyId);

        verify(vacancyMapper).toEntity(request, company);

        verify(vacancyRepository).save(vacancy);
    }

    @Test
    void shouldThrowExceptionWhenCompanyDoesNotExist() {
        Long companyId = 999L;

        VacancyRequest request = new VacancyRequest(
                companyId,
                "Java Backend Intern",
                "https://example.com/vacancy",
                "Helsinki",
                WorkFormat.HYBRID,
                "Internship description"
        );

        when(companyRepository.findById(companyId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> {
                    vacancyService.createVacancy(request);
                }
        );

        assertEquals("Company not found with id: 999", exception.getMessage());

        verify(companyRepository).findById(companyId);

        verify(vacancyMapper, never()).toEntity(eq(request), any(Company.class));
        verify(vacancyRepository, never()).save(any(Vacancy.class));
    }

    @Test
    void shouldReturnAllVacancies() {
        List<Vacancy> vacancies = List.of(
                mock(Vacancy.class),
                mock(Vacancy.class)
        );

        when(vacancyRepository.findAll())
                .thenReturn(vacancies);

        List<Vacancy> result = vacancyService.getAllVacancies();
        assertSame(vacancies, result);

        verify(vacancyRepository).findAll();
    }

    @Test
    void shouldReturnVacancyById() {
        Long vacancyId = 10L;
        Vacancy vacancy = mock(Vacancy.class);

        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(vacancy));

        Vacancy result = vacancyService.getVacancyById(vacancyId);
        assertSame(vacancy, result);

        verify(vacancyRepository).findById(vacancyId);
    }

    @Test
    void shouldThrowExceptionWhenVacancyDoesNotExist() {
        Long vacancyId = 999L;

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vacancyService.getVacancyById(vacancyId)
        );

        assertEquals(
                "Vacancy not found with id: 999",
                exception.getMessage()
        );

        verify(vacancyRepository).findById(vacancyId);
    }

    @Test
    void shouldUpdateVacancy() {
        Long vacancyId = 10L;
        Long companyId = 2L;

        VacancyRequest request = new VacancyRequest(
                companyId,
                "Updated Backend Internship",
                "https://example.com/updated-vacancy",
                "Tampere",
                WorkFormat.REMOTE,
                "Updated description"
        );

        Vacancy existingVacancy = mock(Vacancy.class);
        Company company = mock(Company.class);
        Vacancy savedVacancy = mock(Vacancy.class);

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.of(existingVacancy));

        when(companyRepository.findById(companyId))
                .thenReturn(Optional.of(company));

        when(vacancyRepository.save(existingVacancy))
                .thenReturn(savedVacancy);

        Vacancy result = vacancyService.updateVacancy(vacancyId, request);

        assertSame(savedVacancy, result);

        verify(vacancyRepository).findById(vacancyId);
        verify(companyRepository).findById(companyId);
        verify(vacancyMapper).updateEntity(existingVacancy, request, company);
        verify(vacancyRepository).save(existingVacancy);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingVacancy() {
        Long vacancyId = 999L;

        VacancyRequest request = new VacancyRequest(
                2L,
                "Updated Backend Internship",
                "https://example.com/updated-vacancy",
                "Tampere",
                WorkFormat.REMOTE,
                "Updated description"
        );

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vacancyService.updateVacancy(vacancyId, request));

        assertEquals(
                "Vacancy not found with id: 999",
                exception.getMessage()
        );

        verify(vacancyRepository).findById(vacancyId);
        verifyNoInteractions(companyRepository, vacancyMapper);
        verify(vacancyRepository, never()).save(any(Vacancy.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithMissingCompany() {
        Long vacancyId = 10L;
        Long companyId = 999L;

        VacancyRequest request = new VacancyRequest(
                companyId,
                "Updated Backend Internship",
                "https://example.com/updated-vacancy",
                "Tampere",
                WorkFormat.REMOTE,
                "Updated description"
        );

        Vacancy existingVacancy = mock(Vacancy.class);

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.of(existingVacancy));
        when(companyRepository.findById(companyId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vacancyService.updateVacancy(vacancyId, request));

        assertEquals("Company not found with id: 999",
                exception.getMessage()
        );

        verify(vacancyRepository).findById(vacancyId);
        verify(companyRepository).findById(companyId);
        verifyNoInteractions(vacancyMapper);
        verify(vacancyRepository, never()).save(any(Vacancy.class));
    }

    @Test
    void shouldDeleteVacancy() {
        Long vacancyId = 10L;
        Vacancy vacancy = mock(Vacancy.class);

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.of(vacancy));

        vacancyService.deleteVacancy(vacancyId);

        verify(vacancyRepository).findById(vacancyId);
        verify(vacancyRepository).delete(vacancy);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingVacancy() {
        Long vacancyId = 999L;

        when(vacancyRepository.findById(vacancyId))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vacancyService.deleteVacancy(vacancyId)
        );

        assertEquals("Vacancy not found with id: 999", exception.getMessage());

        verify(vacancyRepository).findById(vacancyId);
        verify(vacancyRepository, never()).delete(any(Vacancy.class));
    }
}
