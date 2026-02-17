package com.abhijot.jobintel.job;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository repo;

    public JobService(JobRepository repo) {
        this.repo = repo;
    }

    public Job create(CreateJobRequest req, String recruiterEmail) {
        Job j = new Job();
        j.setTitle(req.title());
        j.setDescription(req.description());
        j.setCompany(req.company());
        j.setLocation(req.location());
        j.setPostedBy(recruiterEmail);
        j.setStatus(JobStatus.OPEN); // assuming JobStatus is an enum
        return repo.save(j);
    }

    public List<Job> listAll() {
        return repo.findAll();
    }

    public List<Job> myJobs(String recruiterEmail) {
        return repo.findByPostedBy(recruiterEmail);
    }

    public Job updateStatus(Long jobId, String status, String recruiterEmail) {
        Job job = repo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getPostedBy().equals(recruiterEmail)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "Not your job"
            );
        }

        // If status is enum in your Job entity:
        JobStatus newStatus = JobStatus.valueOf(status.toUpperCase());
        job.setStatus(newStatus);

        return repo.save(job);
    }
}
