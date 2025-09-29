package com.example.jobtracker.controller;

import com.example.jobtracker.model.JobVacancy;
import com.example.jobtracker.service.JobVacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job-vacancies")
public class JobVacancyController {

    private final JobVacancyService jobVacancyService;

  
    public JobVacancyController(JobVacancyService jobVacancyService) {
        this.jobVacancyService = jobVacancyService;
    }

    @GetMapping
    public ResponseEntity<List<JobVacancy>> getAllJobVacancies() {
        List<JobVacancy> vacancies = jobVacancyService.getAllJobVacancies();
        return ResponseEntity.ok(vacancies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobVacancy> getJobVacancyById(@PathVariable Long id) {
        Optional<JobVacancy> vacancy = jobVacancyService.getJobVacancyById(id);
        return vacancy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<JobVacancy> createJobVacancy(@RequestBody JobVacancy jobVacancy) {
        JobVacancy savedVacancy = jobVacancyService.saveJobVacancy(jobVacancy);
        return ResponseEntity.ok(savedVacancy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobVacancy> updateJobVacancy(@PathVariable Long id, @RequestBody JobVacancy jobVacancy) {
        if (!jobVacancyService.getJobVacancyById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        jobVacancy.setId(id);
        JobVacancy updatedVacancy = jobVacancyService.saveJobVacancy(jobVacancy);
        return ResponseEntity.ok(updatedVacancy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobVacancy(@PathVariable Long id) {
        if (!jobVacancyService.getJobVacancyById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        jobVacancyService.deleteJobVacancy(id);
        return ResponseEntity.noContent().build();
    }
}
