package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.dto.vacancy.VacancyRequest;
import io.github.koh1o.internshiptrackerapi.entity.Company;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.VacancyMapper;
import io.github.koh1o.internshiptrackerapi.repository.CompanyRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vacancy not found with id: " + id
                ));
    }

    public Vacancy updateVacancy(
            Long id,
            VacancyRequest request
    ) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vacancy not found with id: " + id
                ));
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Company not found with id: " + request.companyId()
                ));

        vacancyMapper.updateEntity(vacancy, request, company);

        Vacancy savedVacancy = vacancyRepository.save(vacancy);

        return savedVacancy;
    }
}
