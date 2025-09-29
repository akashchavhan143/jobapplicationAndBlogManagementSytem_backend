package com.example.jobtracker.service;

import com.example.jobtracker.model.JobVacancy;
import com.example.jobtracker.repository.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobVacancyService {

    private final JobVacancyRepository jobVacancyRepository;

    public JobVacancyService(JobVacancyRepository jobVacancyRepository) {
        this.jobVacancyRepository = jobVacancyRepository;
    }

    public List<JobVacancy> getAllJobVacancies() {
        return jobVacancyRepository.findAllByOrderByPostedAtDesc();
    }

    public Optional<JobVacancy> getJobVacancyById(Long id) {
        return jobVacancyRepository.findById(id);
    }

    public JobVacancy saveJobVacancy(JobVacancy jobVacancy) {
        return jobVacancyRepository.save(jobVacancy);
    }

    public void deleteJobVacancy(Long id) {
        jobVacancyRepository.deleteById(id);
    }

    // Removed getJobVacanciesByTitle since repository method does not exist
}
