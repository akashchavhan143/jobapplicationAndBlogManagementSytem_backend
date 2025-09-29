package com.example.jobtracker.repository;

import com.example.jobtracker.model.JobApplication;
import com.example.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUser(User user);
    List<JobApplication> findByUser_Id(Long userId);
}
