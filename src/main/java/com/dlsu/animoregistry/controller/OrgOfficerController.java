package com.dlsu.animoregistry.controller;

import com.dlsu.animoregistry.dto.LoginRequest;
import com.dlsu.animoregistry.model.OrgOfficer;
import com.dlsu.animoregistry.service.OrgOfficerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officers")
public class OrgOfficerController {

    private final OrgOfficerService officerService;

    public OrgOfficerController(OrgOfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping("/register/{organizationId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrgOfficer register(@PathVariable Long organizationId, @RequestBody OrgOfficer officer) {
        return officerService.register(organizationId, officer);
    }

    @GetMapping
    public List<OrgOfficer> getAll() {
        return officerService.getAll();
    }

    @GetMapping("/{id}")
    public OrgOfficer getById(@PathVariable Long id) {
        return officerService.getById(id);
    }

    @GetMapping("/{id}/dashboard")
    public String getDashboard(@PathVariable Long id) {
        return officerService.getDashboard(id);
    }

    @PostMapping("/login")
    public OrgOfficer login(@RequestBody LoginRequest request) {
        return officerService.login(request.getDlsuEmail(), request.getPassword());
    }
}
