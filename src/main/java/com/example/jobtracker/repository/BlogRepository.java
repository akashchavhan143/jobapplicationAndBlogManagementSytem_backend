package com.example.jobtracker.repository;

import com.example.jobtracker.model.Blog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByUser_Id(Long userId);

    List<Blog> findByApprovedTrueOrderByCreatedAtDesc();
    List<Blog> findByApprovedFalseOrderByCreatedAtDesc();

    List<Blog> findAllByOrderByCreatedAtDesc();

    List<Blog> findByCategory(String category);
    List<Blog> findByTagsContaining(String tag);
    List<Blog> findByTitleContainingIgnoreCase(String title);
}
