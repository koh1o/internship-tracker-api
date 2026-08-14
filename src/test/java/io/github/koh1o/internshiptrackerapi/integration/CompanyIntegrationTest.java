package io.github.koh1o.internshiptrackerapi.integration;

import io.github.koh1o.internshiptrackerapi.configuration.TestcontainersConfiguration;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void cleanDatabase() {
        companyRepository.deleteAll();
    }

    @Test
    void shouldCreateCompany() throws Exception {
        String requestBody = """
                {
                  "name": "JetBrains",
                  "website": "https://www.jetbrains.com",
                  "description": "Software company"
                }
                """;

        mockMvc.perform(
                        post("/api/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("JetBrains"))
                .andExpect(jsonPath("$.website").value("https://www.jetbrains.com"))
                .andExpect(jsonPath("$.description").value("Software company"));

        List<Company> companies = companyRepository.findAll();

        assertEquals(1, companies.size());

        Company savedCompany = companies.getFirst();

        assertEquals("JetBrains", savedCompany.getName());
        assertEquals(
                "https://www.jetbrains.com",
                savedCompany.getWebsite()
        );
        assertEquals(
                "Software company",
                savedCompany.getDescription()
        );
        assertNotNull(savedCompany.getId());
    }

    @Test
    void shouldGetCompanyById() throws Exception {
        Company company = new Company(
                "GitHub",
                "https://github.com",
                "Developer platform"
        );

        Company savedCompany = companyRepository.save(company);

        mockMvc.perform(
                        get("/api/companies/{id}", savedCompany.getId())
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedCompany.getId()))
                .andExpect(jsonPath("$.name").value("GitHub"))
                .andExpect(jsonPath("$.website").value("https://github.com"))
                .andExpect(jsonPath("$.description").value("Developer platform"));

        Optional<Company> foundCompany =
                companyRepository.findById(savedCompany.getId());
        assertTrue(foundCompany.isPresent());
    }

    @Test
    void shouldReturnNotFoundWhenCompanyDoesNotExist() throws Exception {
        long missingCompanyId = 999999L;

        mockMvc.perform(
                        get("/api/companies/{id}", missingCompanyId)
                )
                .andExpect(status().isNotFound());

        assertEquals(0, companyRepository.count());
    }

    @Test
    void shouldRejectCompanyCreationWithBlankName() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "website": "https://invalid.example.com",
                  "description": "Invalid company"
                }
                """;

        mockMvc.perform(
                        post("/api/companies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());

        assertEquals(0, companyRepository.count());
    }

    @Test
    void shouldUpdateCompany() throws Exception {
        Company company = new Company(
                "Old Company",
                "https://old.example.com",
                "Old description"
        );

        Company savedCompany = companyRepository.save(company);

        String requestBody = """
                {
                  "name": "Updated Company",
                  "website": "https://updated.example.com",
                  "description": "Updated description"
                }
                """;

        mockMvc.perform(put("/api/companies/{id}", savedCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedCompany.getId()))
                .andExpect(jsonPath("$.name").value("Updated Company"))
                .andExpect(jsonPath("$.website").value("https://updated.example.com"))
                .andExpect(jsonPath("$.description").value("Updated description"));

        Optional<Company> optionalCompany =
                companyRepository.findById(savedCompany.getId());

        assertTrue(optionalCompany.isPresent());

        Company updatedCompany = optionalCompany.get();

        assertEquals("Updated Company", updatedCompany.getName());
        assertEquals(
                "https://updated.example.com",
                updatedCompany.getWebsite()
        );
        assertEquals(
                "Updated description",
                updatedCompany.getDescription()
        );
    }

    @Test
    void shouldDeleteCompany() throws Exception {
        Company company = new Company(
                "Delete Company",
                "https://delete.example.com",
                "Company to delete"
        );

        Company savedCompany = companyRepository.save(company);

        Long companyId = savedCompany.getId();

        mockMvc.perform(delete("/api/companies/{id}", companyId))
                .andExpect(status().isNoContent());

        Optional<Company> optionalCompany =
                companyRepository.findById(companyId);
        assertTrue(optionalCompany.isEmpty());
    }
}