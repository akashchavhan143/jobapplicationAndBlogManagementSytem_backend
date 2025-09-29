package com.example.jobtracker.service;

import com.example.jobtracker.dto.BlogRequest;
import com.example.jobtracker.dto.BlogResponse;
import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.BlogRepository;
import com.example.jobtracker.repository.UserRepository;
import com.example.jobtracker.storage.StorageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final UserRepository userRepository;

    private final BlogRepository blogRepository;
    private final StorageService storageService;


    public List<Blog> getAllBlogs() {
        return blogRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public Blog saveBlog(Long userId, Blog blog) {
    
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

    blog.setUser(user); // 👈 associate the user with the blog

    return blogRepository.save(blog);
}


    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    public List<Blog> getBlogsByUser(Long userId) {
        
        return blogRepository.findByUser_Id(userId);
    }
    
    public BlogResponse createBlog(BlogRequest request) {
        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setCategory(request.getCategory());

            
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id " + request.getUserId()));

    blog.setUser(user); // 👈 associate the user with the blog
        if (request.getTags() != null) {
            blog.setTags(Arrays.asList(request.getTags().split(",")));
        }
        
        blog.setContent(request.getContent());
        
        if (request.getFeaturedImage() != null && !request.getFeaturedImage().isEmpty()) {
            String imageUrl = storageService.store(request.getFeaturedImage());
            blog.setFeaturedImageUrl(imageUrl);
        }
        
        Blog savedBlog = blogRepository.save(blog);
        return mapToResponse(savedBlog);
    }
    public List<Blog> getApprovedBlogs() {
        return blogRepository.findByApprovedTrueOrderByCreatedAtDesc();
    }

    public List<Blog> getPendingBlogs() {
        return blogRepository.findByApprovedFalseOrderByCreatedAtDesc();
    }

    private BlogResponse mapToResponse(Blog blog) {

        BlogResponse response = new BlogResponse();
        response.setId(blog.getId());
        response.setTitle(blog.getTitle());
        response.setCategory(blog.getCategory());
        response.setTags(blog.getTags());
        response.setContent(blog.getContent());
        response.setFeaturedImageUrl(blog.getFeaturedImageUrl());
        response.setUserId(blog.getUser().getId());
        response.setUsername(blog.getUser().getUsername());
        response.setCreatedAt(blog.getCreatedAt());
        response.setUpdatedAt(blog.getUpdatedAt());
        return response;
    }
}
