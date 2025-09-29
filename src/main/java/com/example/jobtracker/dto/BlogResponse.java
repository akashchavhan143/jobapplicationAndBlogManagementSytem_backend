package com.example.jobtracker.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BlogResponse {
    private Long id;
    private String title;
    private String category;
    private List<String> tags;
    private String content;
    private String featuredImageUrl;

    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
