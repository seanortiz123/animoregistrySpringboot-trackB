package com.dlsu.animoregistry.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class ApplicationForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private LasallianStudent applicant;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    private String answers; // free-text interview/application answers

    private LocalDateTime dateApplied = LocalDateTime.now();

    private LocalDateTime interviewSchedule;

    protected ApplicationForm() {
    }

    public ApplicationForm(LasallianStudent applicant, Organization organization, String answers) {
        this.applicant = applicant;
        this.organization = organization;
        this.answers = answers;
        this.status = ApplicationStatus.PENDING;
        this.dateApplied = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LasallianStudent getApplicant() {
        return applicant;
    }

    public Organization getOrganization() {
        return organization;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public LocalDateTime getDateApplied() {
        return dateApplied;
    }

    public LocalDateTime getInterviewSchedule() {
        return interviewSchedule;
    }

    public void scheduleInterview(LocalDateTime interviewSchedule) {
        this.interviewSchedule = interviewSchedule;
        this.status = ApplicationStatus.INTERVIEW_SCHEDULED;
    }
}
