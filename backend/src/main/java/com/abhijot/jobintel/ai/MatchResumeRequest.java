package com.abhijot.jobintel.ai;

import jakarta.validation.constraints.NotBlank;

public record MatchResumeRequest(
        @NotBlank String jobTitle,
        @NotBlank String jobDescription,
        @NotBlank String resumeText
) {}
