package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
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
}
