package com.example.jobtracker.service;

import com.example.jobtracker.model.JobApplication;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;


    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<JobApplication> findByUserId(Long userId) {
        return jobApplicationRepository.findByUser_Id(userId);
    }
    public List<JobApplication> findByUser(User user) {
        return jobApplicationRepository.findByUser(user);
    }

    public Optional<JobApplication> findById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    public JobApplication createJobApplication(JobApplication jobApplication) {
        return jobApplicationRepository.save(jobApplication);
    }

    public void deleteById(Long id) {
        jobApplicationRepository.deleteById(id);
    }

    public List<JobApplication> findAll() {
        return jobApplicationRepository.findAll();
    }
}
