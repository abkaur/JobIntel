package com.abhijot.jobintel.ai;

import java.util.List;

public record MatchResumeResponse(
        int matchScore,
        List<String> missingSkills,
        List<String> improvementTips
) {}
