package com.example.jobtracker.controller;

import com.example.jobtracker.dto.BlogRequest;
import com.example.jobtracker.dto.BlogResponse;
import com.example.jobtracker.dto.ResponseDto;
import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.User;
import com.example.jobtracker.service.BlogService;
import com.example.jobtracker.service.UserService;
import com.example.jobtracker.storage.StorageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final UserService userService;
    private final StorageService storageService;


    @GetMapping
    public ResponseEntity<ResponseDto<List<Blog>>> getAllBlogs() {
        List<Blog> blogs = blogService.getAllBlogs();
        ResponseDto<List<Blog>> response = new ResponseDto<>(
                "Blogs retrieved successfully",
                blogs,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/approved")
    public ResponseEntity<ResponseDto<List<Blog>>> getApprovedBlogs() {
        List<Blog> blogs = blogService.getApprovedBlogs();
        ResponseDto<List<Blog>> response = new ResponseDto<>(
                "Approved blogs retrieved successfully",
                blogs,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<Blog>> getBlogById(@PathVariable Long id) {
        Optional<Blog> blog = blogService.getBlogById(id);
        if (blog.isPresent()) {
            ResponseDto<Blog> response = new ResponseDto<>(
                    "Blog retrieved successfully",
                    blog.get(),
                    true,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseDto<Blog> response = new ResponseDto<>(
                    "Blog not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDto<List<Blog>>> getBlogsByUser(@PathVariable Long userId) {
        Optional<User> user = userService.findById(userId);
        if (user.isPresent()) {
            List<Blog> blogs = blogService.getBlogsByUser(userId);
            ResponseDto<List<Blog>> response = new ResponseDto<>(
                    "Blogs retrieved successfully",
                    blogs,
                    true,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } else {
            ResponseDto<List<Blog>> response = new ResponseDto<>(
                    "User not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(
            @Valid @ModelAttribute BlogRequest request) throws IOException {
        BlogResponse response = blogService.createBlog(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/create/{userId}")
    public ResponseEntity<ResponseDto<Blog>> createBlog(@PathVariable Long userId,@RequestBody Blog blog) {
        Blog savedBlog = blogService.saveBlog(userId,blog);
        ResponseDto<Blog> response = new ResponseDto<>(
                "Blog created successfully",
                savedBlog,
                true,
                HttpStatus.CREATED
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<Blog>> updateBlog(@PathVariable Long id, @RequestBody Blog blog) {
        if (!blogService.getBlogById(id).isPresent()) {
            ResponseDto<Blog> response = new ResponseDto<>(
                    "Blog not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        blog.setId(id);
        Blog updatedBlog = blogService.saveBlog(1L,blog);
        ResponseDto<Blog> response = new ResponseDto<>(
                "Blog updated successfully",
                updatedBlog,
                true,
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteBlog(@PathVariable Long id) {
        if (!blogService.getBlogById(id).isPresent()) {
            ResponseDto<Void> response = new ResponseDto<>(
                    "Blog not found",
                    null,
                    false,
                    HttpStatus.NOT_FOUND
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        blogService.deleteBlog(id);
        ResponseDto<Void> response = new ResponseDto<>(
                "Blog deleted successfully",
                null,
                true,
                HttpStatus.NO_CONTENT
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
     
    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            // Handle image upload for Editor.js
            String imageUrl = storageService.store(file);
            
            // Return proper JSON object, not stringified JSON
            Map<String, Object> response = new HashMap<>();
            response.put("success", 1);
            
            Map<String, String> fileInfo = new HashMap<>();
            fileInfo.put("url", imageUrl); // Use the full URL returned by storageService
            response.put("file", fileInfo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", 0);
            errorResponse.put("message", "Upload failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
