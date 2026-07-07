package io.github.koh1o.internshiptrackerapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import io.github.koh1o.internshiptrackerapi.entity.Company;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
public class CompanyRepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void shouldInjectCompanyRepository() {
        assertNotNull(companyRepository);
    }

    @Test
    void shouldSaveAndFindCompany() {
        Company company = new Company("JetBrains", null, null);
        Company savedCompany = companyRepository.save(company);
        Optional<Company> foundCompany =
                companyRepository.findById(savedCompany.getId());
        assertTrue(foundCompany.isPresent());
        assertEquals("JetBrains", foundCompany.get().getName());
    }
}
