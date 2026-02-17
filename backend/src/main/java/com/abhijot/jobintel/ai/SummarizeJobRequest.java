package com.abhijot.jobintel.ai;

import jakarta.validation.constraints.NotBlank;

public record SummarizeJobRequest(
        @NotBlank String title,
        @NotBlank String description
) {}

