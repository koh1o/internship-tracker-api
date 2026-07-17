package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyRequest;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.VacancyMapper;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.springframework.stereotype.Service;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final CompanyRepository companyRepository;
    private final VacancyMapper vacancyMapper;

    public VacancyService(
            VacancyRepository vacancyRepository,
            CompanyRepository companyRepository,
            VacancyMapper vacancyMapper
    ) {
        this.vacancyRepository = vacancyRepository;
        this.companyRepository = companyRepository;
        this.vacancyMapper = vacancyMapper;
    }

    public Vacancy createVacancy(VacancyRequest request) {
        Long companyId = request.companyId();

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company not found with id: " + companyId
                ));

        Vacancy vacancy = vacancyMapper.toEntity(request, company);
        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        return savedVacancy;
    }
}
