package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
