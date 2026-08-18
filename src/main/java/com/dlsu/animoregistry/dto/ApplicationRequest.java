package com.dlsu.animoregistry.dto;

/** Request body for a student submitting an application to an organization. */
public class ApplicationRequest {

    private Long applicantId;
    private Long organizationId;
    private String answers;

    public ApplicationRequest() {
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }
}
