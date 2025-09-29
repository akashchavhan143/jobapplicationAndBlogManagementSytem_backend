package com.example.jobtracker.controller;

import com.example.jobtracker.model.BlogComment;
import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.User;
import com.example.jobtracker.service.BlogCommentService;
import com.example.jobtracker.service.BlogService;
import com.example.jobtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog-comments")
public class BlogCommentController {

    private final BlogCommentService blogCommentService;
    private final BlogService blogService;
    private final UserService userService;

    public BlogCommentController(BlogCommentService blogCommentService, BlogService blogService, UserService userService) {
        this.blogCommentService = blogCommentService;
        this.blogService = blogService;
        this.userService = userService;
    }

    @GetMapping
    public List<BlogComment> getAll() {
        return blogCommentService.getAllBlogComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogComment> getById(@PathVariable Long id) {
        Optional<BlogComment> blogComment = blogCommentService.getBlogCommentById(id);
        return blogComment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<List<BlogComment>> getByBlog(@PathVariable Long blogId) {
        Optional<Blog> blog = blogService.getBlogById(blogId);
        if (blog.isPresent()) {
            List<BlogComment> comments = blogCommentService.getBlogCommentsByBlog(blog.get());
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<BlogComment>> getByUser(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            List<BlogComment> comments = blogCommentService.getBlogCommentsByUser(user.get());
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public BlogComment create(@RequestBody BlogComment blogComment) {
        return blogCommentService.saveBlogComment(blogComment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogComment> update(@PathVariable Long id, @RequestBody BlogComment blogComment) {
        Optional<BlogComment> existingComment = blogCommentService.getBlogCommentById(id);
        if (existingComment.isPresent()) {
            blogComment.setId(id);
            BlogComment updatedComment = blogCommentService.saveBlogComment(blogComment);
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<BlogComment> existingComment = blogCommentService.getBlogCommentById(id);
        if (existingComment.isPresent()) {
            blogCommentService.deleteBlogComment(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
