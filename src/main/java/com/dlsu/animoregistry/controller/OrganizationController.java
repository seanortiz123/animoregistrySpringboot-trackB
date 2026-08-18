package com.dlsu.animoregistry.controller;

import com.dlsu.animoregistry.dto.MembershipCapRequest;
import com.dlsu.animoregistry.dto.PaymentRequest;
import com.dlsu.animoregistry.dto.RegistrationStatusRequest;
import com.dlsu.animoregistry.model.Organization;
import com.dlsu.animoregistry.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Organization create(@RequestBody Organization organization) {
        return organizationService.create(organization);
    }

    @GetMapping
    public List<Organization> getAll(@RequestParam(required = false) String category,
                                      @RequestParam(required = false) Boolean openOnly) {
        if (category != null) {
            return organizationService.getByCategory(category);
        }
        if (Boolean.TRUE.equals(openOnly)) {
            return organizationService.getOpenForRegistration();
        }
        return organizationService.getAll();
    }

    @GetMapping("/{id}")
    public Organization getById(@PathVariable Long id) {
        return organizationService.getById(id);
    }

    @PutMapping("/{id}/profile")
    public Organization updateProfile(@PathVariable Long id, @RequestBody Organization updates) {
        return organizationService.updateProfile(id, updates);
    }

    @PatchMapping("/{id}/registration-status")
    public Organization setRegistrationStatus(@PathVariable Long id, @RequestBody RegistrationStatusRequest request) {
        return organizationService.setRegistrationOpen(id, request.isOpen());
    }

    @PatchMapping("/{id}/membership-cap")
    public Organization setMembershipCap(@PathVariable Long id, @RequestBody MembershipCapRequest request) {
        return organizationService.setMembershipCap(id, request.getMembershipCap());
    }

    @PostMapping("/{id}/collect-fee")
    public Map<String, String> collectFee(@PathVariable Long id, @RequestBody PaymentRequest request) {
        String receipt = organizationService.collectMembershipFee(id, request.getPayerName());
        return Map.of("receipt", receipt);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        organizationService.delete(id);
    }
}
