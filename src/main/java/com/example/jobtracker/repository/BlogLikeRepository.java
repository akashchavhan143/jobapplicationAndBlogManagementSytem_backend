package com.example.jobtracker.repository;

import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.BlogLike;
import com.example.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogLikeRepository extends JpaRepository<BlogLike, Long> {
    boolean existsByUserAndBlog(User user, Blog blog);
    void deleteByUserAndBlog(User user, Blog blog);
    long countByBlog(Blog blog);
}
