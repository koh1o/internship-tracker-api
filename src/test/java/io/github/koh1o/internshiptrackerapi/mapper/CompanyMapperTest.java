package io.github.koh1o.internshiptrackerapi.mapper;

import io.github.koh1o.internshiptrackerapi.dto.company.CompanyRequest;
import io.github.koh1o.internshiptrackerapi.dto.company.CompanyResponse;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompanyMapperTest {
    private final CompanyMapper companyMapper = new CompanyMapper();

    @Test
    void shouldMapRequestToEntity() {
        CompanyRequest request = new CompanyRequest(
                "JetBrains",
                "https://www.jetbrains.com",
                "Software development company");
        Company company = companyMapper.toEntity(request);

        assertEquals("JetBrains", company.getName());
        assertEquals("https://www.jetbrains.com", company.getWebsite());
        assertEquals("Software development company", company.getDescription());
    }

    @Test
    void shouldMapEntityToResponse() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 7, 13, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 7, 13, 12, 30);
        Company company = mock(Company.class);

        when(company.getId()).thenReturn(1L);
        when(company.getName()).thenReturn("JetBrains");
        when(company.getWebsite()).thenReturn("https://www.jetbrains.com");
        when(company.getDescription()).thenReturn("Software development company");
        when(company.getCreatedAt()).thenReturn(createdAt);
        when(company.getUpdatedAt()).thenReturn(updatedAt);

        CompanyResponse response = companyMapper.toResponse(company);

        assertEquals(1L, response.id());
        assertEquals("JetBrains", response.name());
        assertEquals("https://www.jetbrains.com", response.website());
        assertEquals("Software development company", response.description());
        assertEquals(createdAt, response.createdAt());
        assertEquals(updatedAt, response.updatedAt());
    }
}
