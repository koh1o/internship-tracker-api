package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationStatusUpdateRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationUpdateRequest;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.ApplicationMapper;
import io.github.koh1o.internshiptrackerapi.repository.ApplicationRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final ApplicationMapper applicationMapper;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            VacancyRepository vacancyRepository,
            ApplicationMapper applicationMapper
    ) {
        this.applicationRepository = applicationRepository;
        this.vacancyRepository = vacancyRepository;
        this.applicationMapper = applicationMapper;
    }

    public Application createApplication(ApplicationRequest request) {
        Vacancy vacancy = vacancyRepository.findById(request.vacancyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vacancy not found with id: " + request.vacancyId()
                ));

        Application application = applicationMapper.toEntity(request, vacancy);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + id
                ));

        return application;
    }

    public Application updateApplication(
            Long id,
            ApplicationUpdateRequest request
    ) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + id
                ));

        Vacancy vacancy = vacancyRepository.findById(request.vacancyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vacancy not found with id: " + request.vacancyId()
                ));

        applicationMapper.updateEntity(application, request, vacancy);
        Application savedApplication = applicationRepository.save(application);

        return savedApplication;
    }

    public Application updateApplicationStatus(
            Long id,
            ApplicationStatusUpdateRequest request
    ) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + id
                ));

        application.setStatus(request.status());

        Application savedApplication = applicationRepository.save(application);

        return savedApplication;
    }

    public void deleteApplication(Long id) {
        Application application = getApplicationById(id);
        applicationRepository.delete(application);
    }
}
