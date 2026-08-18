package com.dlsu.animoregistry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * OOP CONCEPT - INHERITANCE:
 * Inherits idNumber, name, dlsuEmail, password (and their validation) from
 * DLSUUser, and adds student-specific registration data.
 */
@Entity
public class LasallianStudent extends DLSUUser {

    private String college;     // e.g. CCS, COB, CLA
    private String yearLevel;   // e.g. "1st Year", "4th Year"

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ApplicationForm> applications = new ArrayList<>();

    protected LasallianStudent() {
        super();
    }

    public LasallianStudent(String idNumber, String name, String dlsuEmail, String password,
                             String college, String yearLevel) {
        super(idNumber, name, dlsuEmail, password);
        this.college = college;
        this.yearLevel = yearLevel;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public List<ApplicationForm> getApplications() {
        return applications;
    }

    // ---- Polymorphism: Applicant-flavored dashboard ----
    @Override
    public String displayDashboard() {
        long pending = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING)
                .count();
        long accepted = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.ACCEPTED)
                .count();
        return String.format(
                "Applicant Dashboard - %s (%s, %s) | Applications: %d total, %d pending, %d accepted",
                getName(), college, yearLevel, applications.size(), pending, accepted);
    }
}
