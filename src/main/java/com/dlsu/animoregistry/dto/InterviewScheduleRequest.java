package com.dlsu.animoregistry.dto;

import java.time.LocalDateTime;

public class InterviewScheduleRequest {

    private LocalDateTime interviewSchedule;

    public InterviewScheduleRequest() {
    }

    public LocalDateTime getInterviewSchedule() {
        return interviewSchedule;
    }

    public void setInterviewSchedule(LocalDateTime interviewSchedule) {
        this.interviewSchedule = interviewSchedule;
    }
}
