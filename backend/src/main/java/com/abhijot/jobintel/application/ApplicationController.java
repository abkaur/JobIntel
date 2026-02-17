package com.abhijot.jobintel.application;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    // STUDENT applies
    @PostMapping("/apply")
    @PreAuthorize("hasRole('STUDENT')")
    public JobApplicationResponse apply(@RequestBody ApplyRequest req, Authentication auth) {
        JobApplication app = service.apply(req.jobId(), auth.getName(), req.message());
        return JobApplicationResponse.from(app);
    }

    // RECRUITER sees applications (for their jobs only)
    @GetMapping("/applications")
    @PreAuthorize("hasRole('RECRUITER')")
    public List<JobApplicationResponse> recruiterApps(Authentication auth) {
        return service.recruiterApplications(auth.getName()).stream()
                .map(JobApplicationResponse::from)
                .toList();
    }

    // (optional) STUDENT sees own applications
    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('STUDENT')")
    public List<JobApplicationResponse> myApps(Authentication auth) {
        return service.myApplications(auth.getName()).stream()
                .map(JobApplicationResponse::from)
                .toList();
    }
}
