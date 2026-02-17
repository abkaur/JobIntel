package com.abhijot.jobintel.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    boolean existsByJobIdAndStudentEmail(Long jobId, String studentEmail);

    List<JobApplication> findByRecruiterEmail(String recruiterEmail);

    List<JobApplication> findByStudentEmail(String studentEmail);
}
