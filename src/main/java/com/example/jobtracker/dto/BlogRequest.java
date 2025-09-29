package com.example.jobtracker.dto;


import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlogRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String category;
    private String tags;
    private String content;
    private MultipartFile featuredImage;

    private Long userId;

    // Getters
    // public String getTitle() {
    //     return title;
    // }

    // public String getCategory() {
    //     return category;
    // }

    // public String getTags() {
    //     return tags;
    // }

    // public String getContent() {
    //     return content;
    // }

    // public MultipartFile getFeaturedImage() {
    //     return featuredImage;
    // }

    // // Setters
    // public void setTitle(String title) {
    //     this.title = title;
    // }

    // public void setCategory(String category) {
    //     this.category = category;
    // }

    // public void setTags(String tags) {
    //     this.tags = tags;
    // }

    // public void setContent(String content) {
    //     this.content = content;
    // }

    // public void setFeaturedImage(MultipartFile featuredImage) {
    //     this.featuredImage = featuredImage;
    // }
}
