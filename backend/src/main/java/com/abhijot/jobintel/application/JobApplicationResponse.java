package com.abhijot.jobintel.application;

import java.time.Instant;

public record JobApplicationResponse(
        Long id,
        Long jobId,
        String studentEmail,
        String recruiterEmail,
        Instant appliedAt
) {
    public static JobApplicationResponse from(JobApplication a) {
        return new JobApplicationResponse(
                a.getId(),
                a.getJobId(),
                a.getStudentEmail(),
                a.getRecruiterEmail(),
                a.getAppliedAt()
        );
    }
}
