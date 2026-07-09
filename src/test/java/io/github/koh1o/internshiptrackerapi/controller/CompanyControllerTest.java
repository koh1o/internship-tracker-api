package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
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
                .andExpect(status().isNotFound());
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
}
