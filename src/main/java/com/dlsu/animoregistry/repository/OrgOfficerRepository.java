package com.dlsu.animoregistry.repository;

import com.dlsu.animoregistry.model.OrgOfficer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrgOfficerRepository extends JpaRepository<OrgOfficer, Long> {
    Optional<OrgOfficer> findByDlsuEmail(String dlsuEmail);
    List<OrgOfficer> findByOrganizationId(Long organizationId);
}
