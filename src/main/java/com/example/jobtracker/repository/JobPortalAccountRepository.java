package com.example.jobtracker.repository;

import com.example.jobtracker.model.JobPortalAccount;
import com.example.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobPortalAccountRepository extends JpaRepository<JobPortalAccount, Long> {
    List<JobPortalAccount> findByUser(User user);
    List<JobPortalAccount> findByUser_Id(Long userId);

}
