package com.abhijot.jobintel.ai;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    // Recruiter: improve job description
    @PostMapping("/improve-job")
    @PreAuthorize("hasRole('RECRUITER')")
    public ImproveJobResponse improveJob(@Valid @RequestBody ImproveJobRequest req) {
        return aiService.improveJob(req);
    }

    // Student: resume match score
    @PostMapping("/match-resume")
    @PreAuthorize("hasRole('STUDENT')")
    public MatchResumeResponse matchResume(@Valid @RequestBody MatchResumeRequest req) {
        return aiService.matchResume(req);
    }

    // Optional: summarize job for anyone logged in
    @PostMapping("/summarize-job")
    public SummarizeJobResponse summarizeJob(@Valid @RequestBody SummarizeJobRequest req) {
        return aiService.summarizeJob(req);
    }
}
