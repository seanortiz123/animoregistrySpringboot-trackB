package com.dlsu.animoregistry.repository;

import com.dlsu.animoregistry.model.ApplicationForm;
import com.dlsu.animoregistry.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, Long> {
    List<ApplicationForm> findByApplicantId(Long applicantId);
    List<ApplicationForm> findByOrganizationId(Long organizationId);
    List<ApplicationForm> findByOrganizationIdAndStatus(Long organizationId, ApplicationStatus status);
}
