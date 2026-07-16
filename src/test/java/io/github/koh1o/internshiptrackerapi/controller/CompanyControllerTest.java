package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.mapper.CompanyMapper;
import io.github.koh1o.internshiptrackerapi.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
@Import(CompanyMapper.class)
class CompanyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CompanyService companyService;

    @Test
    void shouldReturnAllCompanies() throws Exception {
        Company firstCompany = new Company("JetBrains", null, null);
        Company secondCompany = new Company("Yandex", null, null);
        when(companyService.getAllCompanies())
                .thenReturn(List.of(firstCompany, secondCompany));
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("JetBrains"))
                .andExpect(jsonPath("$[1].name").value("Yandex"));

        verify(companyService).getAllCompanies();
    }

    @Test
    void shouldReturnCompanyById() throws Exception {
        Long companyId = 1L;
        Company company = new Company("JetBrains", null, null);
        when(companyService.getCompanyById(companyId))
                .thenReturn(Optional.of(company));
        mockMvc.perform(get("/api/companies/{id}", companyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("JetBrains"));
        verify(companyService).getCompanyById(companyId);
    }

    @Test
    void shouldReturnNotFoundWhenCompanyDoesNotExist() throws Exception {
        Long companyId = 999L;

        when(companyService.getCompanyById(companyId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/companies/{id}", companyId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not found"))
                .andExpect(jsonPath("$.message").value("Company not found with id: 999"))
                .andExpect(jsonPath("$.path").value("/api/companies/999"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(companyService).getCompanyById(companyId);
    }

    @Test
    void shouldCreateCompany() throws Exception {
        String requestBody = """
                {
                  "name": "JetBrains",
                  "website": "https://www.jetbrains.com",
                  "description": "Software development company"
                }
                """;

        Company createdCompany = new Company(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software development company"
        );

        when(companyService.createCompany(any(Company.class)))
                .thenReturn(createdCompany);
        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("JetBrains"));
        verify(companyService).createCompany(any(Company.class));
    }

    @Test
    void shouldDeleteCompany() throws Exception {
        Long companyId = 1L;

        mockMvc.perform(delete("/api/companies/{id}", companyId))
                .andExpect(status().isNoContent());
        verify(companyService).deleteCompany(companyId);
    }

    @Test
    void shouldUpdateCompany() throws Exception {
        Long companyId = 1L;

        String requestBody = """
                {
                  "name": "JetBrains Updated",
                  "website": "https://www.jetbrains.com",
                  "description": "Updated description"
                }
                """;

        Company updatedCompany = new Company(
                "JetBrains Updated",
                "https://www.jetbrains.com",
                "Updated description"
        );

        when(companyService.updateCompany(eq(companyId), any(Company.class)))
                .thenReturn(Optional.of(updatedCompany));

        mockMvc.perform(put("/api/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("JetBrains Updated"));
        verify(companyService).updateCompany(eq(companyId), any(Company.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingCompany() throws Exception {
        Long companyId = 999L;

        String requestBody = """
                {
                  "name": "Missing Company",
                  "website": "https://example.com",
                  "description": "Updated description"
                }
                """;
        when(companyService.updateCompany(eq(companyId), any(Company.class)))
                .thenReturn(Optional.empty());
        mockMvc.perform(put("/api/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
        verify(companyService).updateCompany(eq(companyId), any(Company.class));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingCompanyWithBlankName() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "website": "https://www.jetbrains.com",
                  "description": "Software development company"
                }
                """;

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(companyService, never()).createCompany(any(Company.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingCompanyWithBlankName() throws Exception {
        Long companyId = 1L;

        String requestBody = """
                {
                  "name": "",
                  "website": "https://www.jetbrains.com",
                  "description": "Software development company"
                }
                """;

        mockMvc.perform(put("/api/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(companyService, never()).updateCompany(eq(companyId), any(Company.class));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingCompanyWithTooLongName() throws Exception {
        String tooLongName = "a".repeat(101);

        String requestBody = """
                {
                  "name": "%s",
                  "website": "https://www.jetbrains.com",
                  "description": "Software development company"
                }
                """.formatted(tooLongName);

        mockMvc.perform(post("/api/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.path").value("/api/companies"))
                .andExpect(jsonPath("$.fieldErrors.name").value("Company name must be at most 100 characters"));

        verify(companyService, never()).createCompany(any(Company.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingCompanyWithTooLongDescription() throws Exception {
        Long companyId = 1L;
        String tooLongDescription = "a".repeat(1001);

        String requestBody = """
                {
                  "name": "JetBrains",
                  "website": "https://www.jetbrains.com",
                  "description": "%s"
                }
                """.formatted(tooLongDescription);

        mockMvc.perform(put("/api/companies/{id}", companyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").value("Invalid request data"))
                .andExpect(jsonPath("$.path").value("/api/companies/1"))
                .andExpect(jsonPath("$.fieldErrors.description")
                        .value("Company description must be at most 1000 characters"));

        verify(companyService, never()).updateCompany(eq(companyId), any(Company.class));
    }
}
