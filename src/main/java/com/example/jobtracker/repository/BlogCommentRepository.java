package com.example.jobtracker.repository;

import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.BlogComment;
import com.example.jobtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {
    List<BlogComment> findByBlogOrderByCreatedAtAsc(Blog blog);
    List<BlogComment> findByUser(User user);
}
