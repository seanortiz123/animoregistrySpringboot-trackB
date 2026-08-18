package com.dlsu.animoregistry.repository;

import com.dlsu.animoregistry.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByCategoryIgnoreCase(String category);
    List<Organization> findByRegistrationOpenTrue();
}
