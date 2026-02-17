package com.abhijot.jobintel.application;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
@Table(
        name = "job_applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "student_email"})
)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @NotBlank
    @Column(name = "recruiter_email", nullable = false)
    private String recruiterEmail;

    @NotBlank
    @Column(name = "student_email", nullable = false)
    private String studentEmail;

    // DB requires NOT NULL
    @Column(name = "applied_at", nullable = false)
    private Instant appliedAt = Instant.now();

    // DB requires NOT NULL (even if db has default now())
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    // DB requires NOT NULL
    @Column(name = "message", nullable = false, length = 3000)
    private String message = "";

    // getters/setters...
    public Long getId() { return id; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getRecruiterEmail() { return recruiterEmail; }
    public void setRecruiterEmail(String recruiterEmail) { this.recruiterEmail = recruiterEmail; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public Instant getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Instant appliedAt) { this.appliedAt = appliedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
