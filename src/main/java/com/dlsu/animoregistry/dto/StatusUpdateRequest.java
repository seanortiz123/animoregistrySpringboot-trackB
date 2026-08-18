package com.dlsu.animoregistry.dto;

import com.dlsu.animoregistry.model.ApplicationStatus;

public class StatusUpdateRequest {

    private ApplicationStatus status;

    public StatusUpdateRequest() {
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
