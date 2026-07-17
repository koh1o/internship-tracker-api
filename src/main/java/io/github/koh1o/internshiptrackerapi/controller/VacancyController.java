package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyRequest;
import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyResponse;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.mapper.VacancyMapper;
import io.github.koh1o.internshiptrackerapi.service.VacancyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;
    private final VacancyMapper vacancyMapper;

    public VacancyController(
            VacancyService vacancyService,
            VacancyMapper vacancyMapper
    ) {
        this.vacancyService = vacancyService;
        this.vacancyMapper = vacancyMapper;
    }

    @PostMapping
    public ResponseEntity<VacancyResponse> createVacancy(
            @Valid @RequestBody VacancyRequest request
    ) {
        Vacancy savedVacancy = vacancyService.createVacancy(request);
        VacancyResponse response = vacancyMapper.toResponse(savedVacancy);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<VacancyResponse> getAllVacancies() {
        List<Vacancy> vacancies = vacancyService.getAllVacancies();
        List<VacancyResponse> responses = vacancies.stream()
                .map(vacancyMapper::toResponse)
                .toList();

        return responses;
    }
}
