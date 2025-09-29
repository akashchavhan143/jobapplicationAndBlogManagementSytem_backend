package com.example.jobtracker.controller;

import com.example.jobtracker.dto.ResponseDto;
import com.example.jobtracker.model.JobApplication;
import com.example.jobtracker.model.User;
import com.example.jobtracker.service.JobApplicationService;
import com.example.jobtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserService userService;

    public JobApplicationController(JobApplicationService jobApplicationService, UserService userService) {
        this.jobApplicationService = jobApplicationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<JobApplication>>> getAllJobApplications() {
        List<JobApplication> jobApplications = jobApplicationService.findAll();
        ResponseDto<List<JobApplication>> response = new ResponseDto<>(
                "Job applications retrieved successfully",
                jobApplications,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<JobApplication>> getJobApplicationById(@PathVariable Long id) {
        Optional<JobApplication> jobApplication = jobApplicationService.findById(id);
        if (jobApplication.isPresent()) {
            ResponseDto<JobApplication> response = new ResponseDto<>(
                    "Job application retrieved successfully",
                    jobApplication.get(),
                    true,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseDto<JobApplication> response = new ResponseDto<>(
                    "Job application not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseDto<JobApplication>> createJobApplication(@PathVariable Long userId, @RequestBody JobApplication jobApplication) {
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            ResponseDto<JobApplication> response = new ResponseDto<>(
                    "User not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        jobApplication.setUser(user.get());
        JobApplication savedJobApplication = jobApplicationService.createJobApplication(jobApplication);

        ResponseDto<JobApplication> response = new ResponseDto<>(
                "Job application created successfully",
                savedJobApplication,
                true,
                HttpStatus.CREATED
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<JobApplication>> updateJobApplication(@PathVariable Long id, @RequestBody JobApplication jobApplication) {
        Optional<JobApplication> existingJobApplication = jobApplicationService.findById(id);
        if (existingJobApplication.isEmpty()) {
            ResponseDto<JobApplication> response = new ResponseDto<>(
                    "Job application not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        JobApplication toUpdate = existingJobApplication.get();
        toUpdate.setCompany(jobApplication.getCompany());
        toUpdate.setRole(jobApplication.getRole());
        toUpdate.setStatus(jobApplication.getStatus());
        toUpdate.setNotes(jobApplication.getNotes());
        JobApplication updatedJobApplication = jobApplicationService.createJobApplication(toUpdate);
        ResponseDto<JobApplication> response = new ResponseDto<>(
                "Job application updated successfully",
                updatedJobApplication,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteJobApplication(@PathVariable Long id) {
        Optional<JobApplication> existingJobApplication = jobApplicationService.findById(id);
        if (existingJobApplication.isEmpty()) {
            ResponseDto<Void> response = new ResponseDto<>(
                    "Job application not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        jobApplicationService.deleteById(id);
        ResponseDto<Void> response = new ResponseDto<>(
                "Job application deleted successfully",
                null,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

@GetMapping("/byUser")
public ResponseEntity<ResponseDto<List<JobApplication>>> getAllJobApplicationsByUserId(
        @RequestParam Long userId) {

    List<JobApplication> jobApplications = jobApplicationService.findByUserId(userId);

    ResponseDto<List<JobApplication>> response = new ResponseDto<>(
            "Job applications retrieved successfully",
            jobApplications,
            true,
            HttpStatus.OK
    );

    return ResponseEntity.ok(response);
}

}
