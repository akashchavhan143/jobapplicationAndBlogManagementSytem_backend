package com.example.jobtracker.service;

import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.BlogComment;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.BlogCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogCommentService {

    private final BlogCommentRepository blogCommentRepository;

    public BlogCommentService(BlogCommentRepository blogCommentRepository) {
        this.blogCommentRepository = blogCommentRepository;
    }

    public List<BlogComment> getAllBlogComments() {
        return blogCommentRepository.findAll();
    }

    public Optional<BlogComment> getBlogCommentById(Long id) {
        return blogCommentRepository.findById(id);
    }

    public List<BlogComment> getBlogCommentsByBlog(Blog blog) {
        return blogCommentRepository.findByBlogOrderByCreatedAtAsc(blog);
    }

    public List<BlogComment> getBlogCommentsByUser(User user) {
        return blogCommentRepository.findByUser(user);
    }

    public BlogComment saveBlogComment(BlogComment blogComment) {
        return blogCommentRepository.save(blogComment);
    }

    public void deleteBlogComment(Long id) {
        blogCommentRepository.deleteById(id);
    }
}
