package com.abhijot.jobintel.job;

import jakarta.validation.constraints.NotBlank;

public record CreateJobRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String company,
        @NotBlank String location
) {}

