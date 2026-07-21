package io.github.koh1o.internshiptrackerapi.controller;

import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationRequest;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationResponse;
import io.github.koh1o.internshiptrackerapi.dto.application.ApplicationUpdateRequest;
import io.github.koh1o.internshiptrackerapi.entity.Application;
import io.github.koh1o.internshiptrackerapi.mapper.ApplicationMapper;
import io.github.koh1o.internshiptrackerapi.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationMapper applicationMapper;

    public ApplicationController(
            ApplicationService applicationService,
            ApplicationMapper applicationMapper
    ) {
        this.applicationService = applicationService;
        this.applicationMapper = applicationMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse createApplication(
            @Valid @RequestBody ApplicationRequest request
    ) {
        Application savedApplication = applicationService.createApplication(request);

        ApplicationResponse response = applicationMapper.toResponse(savedApplication);

        return response;
    }

    @GetMapping
    public List<ApplicationResponse> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();

        return applications.stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ApplicationResponse getApplicationById(
            @PathVariable Long id
    ) {
        Application application = applicationService.getApplicationById(id);

        ApplicationResponse response = applicationMapper.toResponse(application);

        return response;
    }

    @PutMapping("/{id}")
    public ApplicationResponse updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationUpdateRequest request
    ) {
        Application application = applicationService.updateApplication(id, request);

        ApplicationResponse response = applicationMapper.toResponse(application);

        return response;
    }
}
