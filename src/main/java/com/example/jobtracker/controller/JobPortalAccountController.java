package com.example.jobtracker.controller;

import com.example.jobtracker.dto.JobPortalAccountDto;
import com.example.jobtracker.dto.ResponseDto;
import com.example.jobtracker.model.JobPortalAccount;
import com.example.jobtracker.model.User;
import com.example.jobtracker.service.JobPortalAccountService;
import com.example.jobtracker.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job-portal-accounts")
@RequiredArgsConstructor
public class JobPortalAccountController {

    private final JobPortalAccountService jobPortalAccountService;
   private final UserService userService;


    @PostMapping("/create/{userId}")
    public ResponseEntity<ResponseDto<JobPortalAccount>> createJobPortalAccount(
            @PathVariable Long userId,
            @RequestBody JobPortalAccountDto dto) {

        JobPortalAccount account = jobPortalAccountService.saveJobPortalAccount(userId, dto);

        ResponseDto<JobPortalAccount> response = new ResponseDto<>(
                "Job portal account created successfully",
                account,
                true,
                HttpStatus.CREATED
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto<JobPortalAccount>> updateJobPortalAccount(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody JobPortalAccountDto dto) {

        Optional<JobPortalAccount> existingAccount = jobPortalAccountService.getJobPortalAccountById(id);

        if (existingAccount.isPresent()) {
            JobPortalAccount updated = jobPortalAccountService.updateJobPortalAccount(id, userId, dto);

            ResponseDto<JobPortalAccount> response = new ResponseDto<>(
                    "Job portal account updated successfully",
                    updated,
                    true,
                    HttpStatus.OK
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ResponseDto<JobPortalAccount> response = new ResponseDto<>(
                    "Account not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<JobPortalAccount>> getById(@PathVariable Long id) {
        Optional<JobPortalAccount> account = jobPortalAccountService.getJobPortalAccountById(id);

        if (account.isPresent()) {
            ResponseDto<JobPortalAccount> response = new ResponseDto<>(
                    "Account found",
                    account.get(),
                    true,
                    HttpStatus.OK
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ResponseDto<JobPortalAccount> response = new ResponseDto<>(
                    "Account not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> delete(@PathVariable Long id) {
        Optional<JobPortalAccount> existingAccount = jobPortalAccountService.getJobPortalAccountById(id);

        if (existingAccount.isPresent()) {
            jobPortalAccountService.deleteJobPortalAccount(id);

            ResponseDto<Void> response = new ResponseDto<>(
                    "Job portal account deleted successfully",
                    null,
                    true,
                    HttpStatus.OK
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ResponseDto<Void> response = new ResponseDto<>(
                    "Account not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

   @GetMapping("/user/{userId}")
public ResponseEntity<ResponseDto<List<JobPortalAccount>>> getByUser(@PathVariable Long userId) {
    Optional<User> user = userService.findById(userId);

    ResponseDto<List<JobPortalAccount>> response;

    if (user.isPresent()) {
        List<JobPortalAccount> accounts = jobPortalAccountService.getJobPortalAccountsByUser(user.get().getId());
        response = new ResponseDto<>(
                "Accounts fetched successfully",
                accounts,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    } else {
        response = new ResponseDto<>(
                "User not found",
                null,
                false,
                HttpStatus.NOT_FOUND
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

}
