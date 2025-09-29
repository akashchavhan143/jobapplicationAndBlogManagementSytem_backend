package com.example.jobtracker.service;

import com.example.jobtracker.dto.JobPortalAccountDto;
import com.example.jobtracker.model.JobPortalAccount;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.JobPortalAccountRepository;
import com.example.jobtracker.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPortalAccountService {

    private final PasswordEncoder passwordEncoder;

    private final JobPortalAccountRepository jobPortalAccountRepository;
     private final UserRepository userRepository;
    public JobPortalAccountService(JobPortalAccountRepository jobPortalAccountRepository,UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jobPortalAccountRepository = jobPortalAccountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<JobPortalAccount> getAllJobPortalAccounts() {
        return jobPortalAccountRepository.findAll();
    }

    public Optional<JobPortalAccount> getJobPortalAccountById(Long id) {
        return jobPortalAccountRepository.findById(id);
    }

    public List<JobPortalAccount> getJobPortalAccountsByUser(Long userId) {
        return jobPortalAccountRepository.findByUser_Id(userId);
    }
    public JobPortalAccount saveJobPortalAccount(Long userId, JobPortalAccountDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        JobPortalAccount account = new JobPortalAccount();
        account.setUsername(dto.getUsername());
        account.setLink(dto.getLink());
        account.setEncryptedPassword(passwordEncoder.encode(dto.getPassword())); // 🔐 encrypt password
        account.setUser(user);

        return jobPortalAccountRepository.save(account);
    }

    public void deleteJobPortalAccount(Long id) {
        jobPortalAccountRepository.deleteById(id);
    }
 public JobPortalAccount updateJobPortalAccount(Long id, Long userId, JobPortalAccountDto dto) {
        JobPortalAccount existing = jobPortalAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job portal account not found with id " + id));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        existing.setUsername(dto.getUsername());
        existing.setLink(dto.getLink());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setEncryptedPassword(passwordEncoder.encode(dto.getPassword())); // update password only if provided
        }

        existing.setUser(user);

        return jobPortalAccountRepository.save(existing);
    }
}
