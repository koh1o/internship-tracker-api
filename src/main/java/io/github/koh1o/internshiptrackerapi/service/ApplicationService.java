package io.github.koh1o.internshiptrackerapi.service;

import io.github.koh1o.internshiptrackerapi.dto.PagedResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationStatusUpdateRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationUpdateRequest;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.entity.ApplicationStatus;
import io.github.koh1o.internshiptrackerapi.entity.Vacancy;
import io.github.koh1o.internshiptrackerapi.exception.InvalidApplicationDataException;
import io.github.koh1o.internshiptrackerapi.exception.ResourceNotFoundException;
import io.github.koh1o.internshiptrackerapi.mapper.ApplicationMapper;
import io.github.koh1o.internshiptrackerapi.repository.ApplicationRepository;
import io.github.koh1o.internshiptrackerapi.repository.VacancyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final ApplicationMapper applicationMapper;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "createdAt",
            "appliedAt",
            "nextContactAt",
            "status"
    );

    public ApplicationService(
            ApplicationRepository applicationRepository,
            VacancyRepository vacancyRepository,
            ApplicationMapper applicationMapper
    ) {
        this.applicationRepository = applicationRepository;
        this.vacancyRepository = vacancyRepository;
        this.applicationMapper = applicationMapper;
    }

    public PagedResponse<ApplicationResponse> getAllApplications(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        if (sortBy == null || !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new InvalidApplicationDataException(
                    "Unsupported sort field: " + sortBy
            );
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new InvalidApplicationDataException(
                    "Unsupported sort direction: " + direction
            );
        }

        Sort sort = Sort.by(sortDirection, sortBy);

        Pageable pageable = PageRequest.of(
                page,
                size,
                sort
        );

        Page<Application> applicationPage =
                applicationRepository.findAll(pageable);

        List<ApplicationResponse> content = applicationPage.getContent()
                .stream()
                .map(applicationMapper::toResponse)
                .toList();

        return new PagedResponse<>(
                content,
                applicationPage.getNumber(),
                applicationPage.getSize(),
                applicationPage.getTotalElements(),
                applicationPage.getTotalPages()
        );
    }

    public Application createApplication(ApplicationRequest request) {
        validateDates(
                request.appliedAt(),
                request.nextContactAt()
        );
        validateAppliedAtForStatus(
                request.status(),
                request.appliedAt()
        );
        Vacancy vacancy = vacancyRepository.findById(request.vacancyId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vacancy not found with id: " + request.vacancyId()
                ));

        Application application = applicationMapper.toEntity(request, vacancy);

        Application savedApplication = applicationRepository.save(application);
        return savedApplication;
    }

    public PagedResponse<ApplicationResponse> getAllApplications(
            int page,
            int size
    ) {
        return getAllApplications(
                page,
                size,
                "createdAt",
                "DESC"
        );
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
        validateDates(request.appliedAt(), request.nextContactAt());
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Application not found with id: " + id
                ));

        validateAppliedAtForStatus(
                application.getStatus(),
                request.appliedAt()
        );

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

        ApplicationStatus currentStatus = application.getStatus();

        if (currentStatus == request.status()) {
            return application;
        }

        validateStatusTransition(
                currentStatus,
                request.status()
        );

        validateAppliedAtForStatus(
                request.status(),
                application.getAppliedAt()
        );

        application.setStatus(request.status());

        Application savedApplication = applicationRepository.save(application);

        return savedApplication;
    }

    public void deleteApplication(Long id) {
        Application application = getApplicationById(id);
        applicationRepository.delete(application);
    }

    private void validateDates(
            LocalDateTime appliedAt,
            LocalDateTime nextContactAt
    ) {
        if (appliedAt != null
                && nextContactAt != null
                && nextContactAt.isBefore(appliedAt)) {
            throw new InvalidApplicationDataException(
                    "Next contact date must not be before applied date"
            );
        }
    }

    private void validateStatusTransition(
            ApplicationStatus currentStatus,
            ApplicationStatus newStatus
    ) {
        boolean allowed = switch (currentStatus) {
            case PLANNED ->
                    newStatus == ApplicationStatus.APPLIED
                            || newStatus == ApplicationStatus.WITHDRAWN;

            case APPLIED ->
                    newStatus == ApplicationStatus.TEST_TASK
                            || newStatus == ApplicationStatus.INTERVIEW
                            || newStatus == ApplicationStatus.REJECTED
                            || newStatus == ApplicationStatus.WITHDRAWN;

            case TEST_TASK ->
                    newStatus == ApplicationStatus.INTERVIEW
                            || newStatus == ApplicationStatus.REJECTED
                            || newStatus == ApplicationStatus.WITHDRAWN;

            case INTERVIEW ->
                    newStatus == ApplicationStatus.OFFER
                            || newStatus == ApplicationStatus.REJECTED
                            || newStatus == ApplicationStatus.WITHDRAWN;

            case OFFER, REJECTED, WITHDRAWN -> false;
        };

        if (!allowed) {
            throw new InvalidApplicationDataException(
                    "Cannot change status from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }
    }

    private void validateAppliedAtForStatus(
            ApplicationStatus status,
            LocalDateTime appliedAt
    ) {
        if (status != ApplicationStatus.PLANNED && appliedAt == null) {
            throw new InvalidApplicationDataException(
                    "Applied date is required for status " + status
            );
        }
    }
}
