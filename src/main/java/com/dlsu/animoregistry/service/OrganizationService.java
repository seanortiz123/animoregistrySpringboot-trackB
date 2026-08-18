package com.dlsu.animoregistry.service;

import com.dlsu.animoregistry.exception.ResourceNotFoundException;
import com.dlsu.animoregistry.model.Organization;
import com.dlsu.animoregistry.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Organization create(Organization organization) {
        return organizationRepository.save(organization);
    }

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    public List<Organization> getOpenForRegistration() {
        return organizationRepository.findByRegistrationOpenTrue();
    }

    public List<Organization> getByCategory(String category) {
        return organizationRepository.findByCategoryIgnoreCase(category);
    }

    public Organization getById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + id));
    }

    // Executive Board Member user story: toggle registration status
    public Organization setRegistrationOpen(Long id, boolean open) {
        Organization organization = getById(id);
        if (open) {
            organization.openRegistration();
        } else {
            organization.closeRegistration();
        }
        return organizationRepository.save(organization);
    }

    // Org Officer user story: set membership cap
    public Organization setMembershipCap(Long id, int cap) {
        Organization organization = getById(id);
        organization.setMembershipCap(cap);
        return organizationRepository.save(organization);
    }

    // Central Committee Member user story: edit org profile
    public Organization updateProfile(Long id, Organization updates) {
        Organization organization = getById(id);
        if (updates.getDescription() != null) organization.setDescription(updates.getDescription());
        if (updates.getLogoUrl() != null) organization.setLogoUrl(updates.getLogoUrl());
        if (updates.getSocialMediaHandle() != null) organization.setSocialMediaHandle(updates.getSocialMediaHandle());
        if (updates.getCategory() != null) organization.setCategory(updates.getCategory());
        return organizationRepository.save(organization);
    }

    // Abstraction in action: collect a membership fee without the caller
    // needing to know whether it's cash or a digital bank transfer
    public String collectMembershipFee(Long id, String payerName) {
        Organization organization = getById(id);
        return organization.collectMembershipFee(payerName);
    }

    public void delete(Long id) {
        Organization organization = getById(id);
        organizationRepository.delete(organization);
    }
}
