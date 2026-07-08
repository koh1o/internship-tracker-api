package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void shouldCreateCompany() {
        Company company = new Company("JetBrains", null, null);
        Company savedCompany = new Company("JetBrains", null, null);
        when(companyRepository.save(company))
                .thenReturn(savedCompany);
        Company result = companyService.createCompany(company);
        assertSame(savedCompany, result);
        verify(companyRepository).save(company);
    }

    @Test
    void shouldReturnAllCompanies() {
        Company firstCompany = new Company("JetBrains", null, null);
        Company secondCompany = new Company("Yandex", null, null);

        List<Company> companies = List.of(firstCompany, secondCompany);

        when(companyRepository.findAll())
                .thenReturn(companies);
        List<Company> result = companyService.getAllCompanies();
        assertSame(companies, result);
        verify(companyRepository).findAll();
    }

    @Test
    void shouldReturnCompanyById() {
        Company company = new Company("JetBrains", null, null);
        Long id = 1L;

        when(companyRepository.findById(id))
                .thenReturn(Optional.of(company));
        Optional<Company> result = companyService.getCompanyById(id);
        assertTrue(result.isPresent());
        assertSame(company, result.get());
        verify(companyRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenCompanyDoesNotExist() {
        Long id = 1L;

        when(companyRepository.findById(id))
                .thenReturn(Optional.empty());
        Optional<Company> result = companyService.getCompanyById(id);
        assertTrue(result.isEmpty());
        verify(companyRepository).findById(id);
    }

    @Test
    void shouldDeleteCompanyById() {
        Long id = 1L;
        companyService.deleteCompany(id);
        verify(companyRepository).deleteById(id);
    }

    @Test
    void shouldUpdateCompanyWhenCompanyExists() {
        Long id = 1L;

        Company existingCompany = new Company(
                "Old company",
                "https://old.example.com",
                "Old description"
        );

        Company updatedCompany = new Company(
                "Updated company",
                "https://updated.example.com",
                "Updated description"
        );

        when(companyRepository.findById(id))
                .thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(existingCompany))
                .thenReturn(existingCompany);
        Optional<Company> result = companyService.updateCompany(id, updatedCompany);

        assertTrue(result.isPresent());

        Company resultCompany = result.get();

        String updatedName = resultCompany.getName();
        String updatedWebsite = resultCompany.getWebsite();
        String updatedDescription = resultCompany.getDescription();

        assertEquals("Updated company", updatedName);
        assertEquals("https://updated.example.com", updatedWebsite);
        assertEquals("Updated description", updatedDescription);

        verify(companyRepository).findById(id);
        assertSame(existingCompany, resultCompany);
        verify(companyRepository).save(existingCompany);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingMissingCompany() {
        Long id = 1L;
        Company updatedCompany = new Company(
                "Updated company",
                "https://updated.example.com",
                "Updated description"
        );
        when(companyRepository.findById(id))
                .thenReturn(Optional.empty());

        Optional<Company> result = companyService.updateCompany(id, updatedCompany);
        assertTrue(result.isEmpty());

        verify(companyRepository).findById(id);
        verify(companyRepository, never()).save(any(Company.class));
    }
}
