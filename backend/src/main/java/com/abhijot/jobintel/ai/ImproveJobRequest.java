package com.abhijot.jobintel.ai;

import jakarta.validation.constraints.NotBlank;

public record ImproveJobRequest(
        @NotBlank String title,
        @NotBlank String description
) {}
