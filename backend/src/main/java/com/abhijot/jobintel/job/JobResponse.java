package com.abhijot.jobintel.job;

import java.time.Instant;

public record JobResponse(
        Long id,
        String title,
        String description,
        String company,
        String location,
        JobStatus status,
        Instant createdAt,
        String postedBy
) {
    public static JobResponse from(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCompany(),
                job.getLocation(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getPostedBy()
        );
    }
}
