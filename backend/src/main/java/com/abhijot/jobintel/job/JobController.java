package com.abhijot.jobintel.job;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public JobResponse create(@Valid @RequestBody CreateJobRequest req, Authentication auth) {
        Job saved = jobService.create(req, auth.getName());
        return JobResponse.from(saved);
    }

    @GetMapping
    public List<JobResponse> list() {
        return jobService.listAll().stream().map(JobResponse::from).toList();
    }

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public List<JobResponse> myJobs(Authentication auth) {
        return jobService.myJobs(auth.getName()).stream().map(JobResponse::from).toList();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public JobResponse updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        Job updated = jobService.updateStatus(
                id,
                body.getOrDefault("status", "OPEN"),
                auth.getName()
        );
        return JobResponse.from(updated);
    }
}
