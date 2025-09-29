package com.example.jobtracker.repository;

import com.example.jobtracker.model.JobVacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobVacancyRepository extends JpaRepository<JobVacancy, Long> {
    List<JobVacancy> findAllByOrderByPostedAtDesc();
}
