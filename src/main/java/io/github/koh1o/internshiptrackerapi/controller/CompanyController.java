package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.dto.company.CompanyResponse;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.mapper.CompanyMapper;
import io.github.koh1o.internshiptrackerapi.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    public CompanyController(
            CompanyService companyService,
            CompanyMapper companyMapper
    ) {
        this.companyService = companyService;
        this.companyMapper = companyMapper;
    }

    @GetMapping
    public List<CompanyResponse> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();

        return companies.stream()
                .map(companyMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(
            @RequestBody Company company
    ) {
        Company savedCompany = companyService.createCompany(company);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCompany);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(
            @PathVariable Long id
    ) {
        Optional<Company> companyOptional = companyService.getCompanyById(id);
        if (companyOptional.isPresent()) {
            CompanyResponse response = companyMapper.toResponse(companyOptional.get());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long id
    ) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Long id,
            @RequestBody Company company
    ) {
        Optional<Company> updatedCompanyOptional = companyService.updateCompany(id, company);
        if (updatedCompanyOptional.isPresent()) {
            return ResponseEntity.ok(updatedCompanyOptional.get());
        }
        return ResponseEntity.notFound().build();
    }
}
