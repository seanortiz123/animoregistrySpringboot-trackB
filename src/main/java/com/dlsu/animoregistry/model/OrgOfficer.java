package com.dlsu.animoregistry.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * OOP CONCEPT - INHERITANCE:
 * Inherits idNumber, name, dlsuEmail, password from DLSUUser, and adds
 * org-specific data: which organization they represent and their position.
 */
@Entity
public class OrgOfficer extends DLSUUser {

    private String position; // e.g. "President", "VP for Membership", "Committee Head"

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    protected OrgOfficer() {
        super();
    }

    public OrgOfficer(String idNumber, String name, String dlsuEmail, String password,
                       Organization organization, String position) {
        super(idNumber, name, dlsuEmail, password);
        this.organization = organization;
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // ---- Polymorphism: Officer-flavored dashboard ----
    @Override
    public String displayDashboard() {
        String orgName = organization != null ? organization.getName() : "(no organization assigned)";
        int pendingCount = organization != null
                ? (int) organization.getApplications().stream()
                    .filter(a -> a.getStatus() == ApplicationStatus.PENDING)
                    .count()
                : 0;
        return String.format(
                "Officer Dashboard - %s, %s of %s | %d applications awaiting review",
                getName(), position, orgName, pendingCount);
    }
}
