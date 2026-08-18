package com.dlsu.animoregistry.service;

import com.dlsu.animoregistry.exception.ResourceNotFoundException;
import com.dlsu.animoregistry.model.OrgOfficer;
import com.dlsu.animoregistry.model.Organization;
import com.dlsu.animoregistry.repository.OrgOfficerRepository;
import com.dlsu.animoregistry.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrgOfficerService {

    private final OrgOfficerRepository officerRepository;
    private final OrganizationRepository organizationRepository;

    public OrgOfficerService(OrgOfficerRepository officerRepository, OrganizationRepository organizationRepository) {
        this.officerRepository = officerRepository;
        this.organizationRepository = organizationRepository;
    }

    public OrgOfficer register(Long organizationId, OrgOfficer officer) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));
        officerRepository.findByDlsuEmail(officer.getDlsuEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("An account with this DLSU email already exists.");
        });
        officer.setOrganization(organization);
        return officerRepository.save(officer);
    }

    public List<OrgOfficer> getAll() {
        return officerRepository.findAll();
    }

    public OrgOfficer getById(Long id) {
        return officerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Officer not found with id: " + id));
    }

    public String getDashboard(Long id) {
        return getById(id).displayDashboard();
    }

    public OrgOfficer login(String dlsuEmail, String password) {
        OrgOfficer officer = officerRepository.findByDlsuEmail(dlsuEmail)
                .orElseThrow(() -> new IllegalArgumentException("No account found for that email."));
        if (!officer.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
        return officer;
    }
}
