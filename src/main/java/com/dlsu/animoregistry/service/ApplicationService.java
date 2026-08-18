package com.dlsu.animoregistry.service;

import com.dlsu.animoregistry.dto.ApplicationRequest;
import com.dlsu.animoregistry.exception.ResourceNotFoundException;
import com.dlsu.animoregistry.model.ApplicationForm;
import com.dlsu.animoregistry.model.ApplicationStatus;
import com.dlsu.animoregistry.model.LasallianStudent;
import com.dlsu.animoregistry.model.Organization;
import com.dlsu.animoregistry.repository.ApplicationFormRepository;
import com.dlsu.animoregistry.repository.LasallianStudentRepository;
import com.dlsu.animoregistry.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationFormRepository applicationRepository;
    private final LasallianStudentRepository studentRepository;
    private final OrganizationRepository organizationRepository;

    public ApplicationService(ApplicationFormRepository applicationRepository,
                               LasallianStudentRepository studentRepository,
                               OrganizationRepository organizationRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.organizationRepository = organizationRepository;
    }

    // Student user story: submit an application to an organization
    public ApplicationForm submitApplication(ApplicationRequest request) {
        LasallianStudent applicant = studentRepository.findById(request.getApplicantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + request.getApplicantId()));
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Organization not found with id: " + request.getOrganizationId()));

        if (!organization.isRegistrationOpen()) {
            throw new IllegalStateException(
                    "Recruitment for " + organization.getName() + " is currently closed.");
        }
        if (!organization.hasAvailableSlot()) {
            throw new IllegalStateException(
                    "No slots remaining for " + organization.getName() + ".");
        }

        ApplicationForm application = new ApplicationForm(applicant, organization, request.getAnswers());
        return applicationRepository.save(application);
    }

    public List<ApplicationForm> getByStudent(Long studentId) {
        return applicationRepository.findByApplicantId(studentId);
    }

    public List<ApplicationForm> getByOrganization(Long organizationId) {
        return applicationRepository.findByOrganizationId(organizationId);
    }

    public List<ApplicationForm> getByOrganizationAndStatus(Long organizationId, ApplicationStatus status) {
        return applicationRepository.findByOrganizationIdAndStatus(organizationId, status);
    }

    public ApplicationForm getById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + id));
    }

    // Org Officer user story: schedule an interview
    public ApplicationForm scheduleInterview(Long applicationId, LocalDateTime interviewSchedule) {
        ApplicationForm application = getById(applicationId);
        application.scheduleInterview(interviewSchedule);
        return applicationRepository.save(application);
    }

    // Org Officer user story: accept or deny an application
    public ApplicationForm updateStatus(Long applicationId, ApplicationStatus newStatus) {
        ApplicationForm application = getById(applicationId);

        if (newStatus == ApplicationStatus.ACCEPTED
                && application.getStatus() != ApplicationStatus.ACCEPTED) {
            Organization organization = application.getOrganization();
            if (!organization.hasAvailableSlot()) {
                throw new IllegalStateException(
                        "Cannot accept applicant: " + organization.getName() + " has reached its membership cap.");
            }
            organization.incrementMemberCount();
            organizationRepository.save(organization);
        }

        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}
