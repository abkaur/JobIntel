package com.abhijot.jobintel.ai;

import java.util.List;

public record ImproveJobResponse(
        String improvedDescription,
        List<String> keySkills
) {}
