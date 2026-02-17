package com.abhijot.jobintel.application;

import jakarta.validation.constraints.NotNull;

public record ApplyRequest(
        @NotNull Long jobId,
        String message
) {}
