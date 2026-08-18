package com.dlsu.animoregistry.controller;

import com.dlsu.animoregistry.dto.ApplicationRequest;
import com.dlsu.animoregistry.dto.InterviewScheduleRequest;
import com.dlsu.animoregistry.dto.StatusUpdateRequest;
import com.dlsu.animoregistry.model.ApplicationForm;
import com.dlsu.animoregistry.model.ApplicationStatus;
import com.dlsu.animoregistry.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // Student user story: submit an application
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationForm submit(@RequestBody ApplicationRequest request) {
        return applicationService.submitApplication(request);
    }

    @GetMapping("/{id}")
    public ApplicationForm getById(@PathVariable Long id) {
        return applicationService.getById(id);
    }

    // Student dashboard: track status of my own applications
    @GetMapping("/student/{studentId}")
    public List<ApplicationForm> getByStudent(@PathVariable Long studentId) {
        return applicationService.getByStudent(studentId);
    }

    // Org Officer: view applicants for their organization (optionally filter by status)
    @GetMapping("/organization/{organizationId}")
    public List<ApplicationForm> getByOrganization(@PathVariable Long organizationId,
                                                    @RequestParam(required = false) ApplicationStatus status) {
        if (status != null) {
            return applicationService.getByOrganizationAndStatus(organizationId, status);
        }
        return applicationService.getByOrganization(organizationId);
    }

    // Org Officer: schedule an interview
    @PatchMapping("/{id}/interview")
    public ApplicationForm scheduleInterview(@PathVariable Long id, @RequestBody InterviewScheduleRequest request) {
        return applicationService.scheduleInterview(id, request.getInterviewSchedule());
    }

    // Org Officer: accept or reject an application
    @PatchMapping("/{id}/status")
    public ApplicationForm updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        return applicationService.updateStatus(id, request.getStatus());
    }
}
