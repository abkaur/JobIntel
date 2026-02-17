package com.abhijot.jobintel.application;

import com.abhijot.jobintel.job.Job;
import com.abhijot.jobintel.job.JobRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ApplicationService {

    private final JobApplicationRepository repo;
    private final JobRepository jobRepo;

    public ApplicationService(JobApplicationRepository repo, JobRepository jobRepo) {
        this.repo = repo;
        this.jobRepo = jobRepo;
    }

    public JobApplication apply(Long jobId, String studentEmail, String message) {

        // prevent duplicate apply
        if (repo.existsByJobIdAndStudentEmail(jobId, studentEmail)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT,
                    "You have already applied to this job"
            );
        }

        // fetch job (to get recruiter email)
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Job not found"
        ));


        JobApplication a = new JobApplication();
        a.setJobId(jobId);
        a.setStudentEmail(studentEmail);
        a.setRecruiterEmail(job.getPostedBy());

        a.setMessage(message == null ? "" : message);

        a.setAppliedAt(Instant.now());
        a.setCreatedAt(Instant.now());

        return repo.save(a);
    }

    public List<JobApplication> recruiterApplications(String recruiterEmail) {
        return repo.findByRecruiterEmail(recruiterEmail);
    }

    public List<JobApplication> myApplications(String studentEmail) {
        return repo.findByStudentEmail(studentEmail);
    }
}
