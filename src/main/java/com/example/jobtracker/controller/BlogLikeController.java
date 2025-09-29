package com.example.jobtracker.controller;

import com.example.jobtracker.model.BlogLike;
import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.User;
import com.example.jobtracker.service.BlogLikeService;
import com.example.jobtracker.service.BlogService;
import com.example.jobtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog-likes")
public class BlogLikeController {

    private final BlogLikeService blogLikeService;
    private final BlogService blogService;
    private final UserService userService;

    @Autowired
    public BlogLikeController(BlogLikeService blogLikeService, BlogService blogService, UserService userService) {
        this.blogLikeService = blogLikeService;
        this.blogService = blogService;
        this.userService = userService;
    }

    @GetMapping
    public List<BlogLike> getAll() {
        return blogLikeService.getAllBlogLikes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogLike> getById(@PathVariable Long id) {
        Optional<BlogLike> blogLike = blogLikeService.getBlogLikeById(id);
        return blogLike.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/blog/{blogId}/count")
    public ResponseEntity<Long> getCountByBlog(@PathVariable Long blogId) {
        Optional<Blog> blog = blogService.getBlogById(blogId);
        if (blog.isPresent()) {
            long count = blogLikeService.countLikesByBlog(blog.get());
            return ResponseEntity.ok(count);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/blog/{blogId}/user/{username}/exists")
    public ResponseEntity<Boolean> existsByUserAndBlog(@PathVariable Long blogId, @PathVariable String username) {
        Optional<Blog> blog = blogService.getBlogById(blogId);
        Optional<User> user = userService.findByUsername(username);
        if (blog.isPresent() && user.isPresent()) {
            boolean exists = blogLikeService.existsByUserAndBlog(user.get(), blog.get());
            return ResponseEntity.ok(exists);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public BlogLike create(@RequestBody BlogLike blogLike) {
        return blogLikeService.saveBlogLike(blogLike);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<BlogLike> existingLike = blogLikeService.getBlogLikeById(id);
        if (existingLike.isPresent()) {
            blogLikeService.deleteBlogLike(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/blog/{blogId}/user/{username}")
    public ResponseEntity<Void> deleteByUserAndBlog(@PathVariable Long blogId, @PathVariable String username) {
        Optional<Blog> blog = blogService.getBlogById(blogId);
        Optional<User> user = userService.findByUsername(username);
        if (blog.isPresent() && user.isPresent()) {
            blogLikeService.deleteByUserAndBlog(user.get(), blog.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
