package com.example.jobtracker.service;

import com.example.jobtracker.model.Blog;
import com.example.jobtracker.model.BlogLike;
import com.example.jobtracker.model.User;
import com.example.jobtracker.repository.BlogLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogLikeService {

    private final BlogLikeRepository blogLikeRepository;

   
    public BlogLikeService(BlogLikeRepository blogLikeRepository) {
        this.blogLikeRepository = blogLikeRepository;
    }

    public List<BlogLike> getAllBlogLikes() {
        return blogLikeRepository.findAll();
    }

    public Optional<BlogLike> getBlogLikeById(Long id) {
        return blogLikeRepository.findById(id);
    }

    public long countLikesByBlog(Blog blog) {
        return blogLikeRepository.countByBlog(blog);
    }

    public boolean existsByUserAndBlog(User user, Blog blog) {
        return blogLikeRepository.existsByUserAndBlog(user, blog);
    }

    public BlogLike saveBlogLike(BlogLike blogLike) {
        return blogLikeRepository.save(blogLike);
    }

    public void deleteBlogLike(Long id) {
        blogLikeRepository.deleteById(id);
    }

    public void deleteByUserAndBlog(User user, Blog blog) {
        blogLikeRepository.deleteByUserAndBlog(user, blog);
    }
}
