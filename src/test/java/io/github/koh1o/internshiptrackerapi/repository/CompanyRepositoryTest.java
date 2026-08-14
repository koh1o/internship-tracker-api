package io.github.koh1o.internshiptrackerapi.repository;

import io.github.koh1o.internshiptrackerapi.configuration.TestcontainersConfiguration;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Import(TestcontainersConfiguration.class)
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
