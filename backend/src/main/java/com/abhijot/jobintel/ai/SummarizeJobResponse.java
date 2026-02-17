package com.abhijot.jobintel.ai;

import java.util.List;

public record SummarizeJobResponse(
        List<String> bullets,
        List<String> keySkills
) {}
