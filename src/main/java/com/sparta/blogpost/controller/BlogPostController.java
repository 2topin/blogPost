package com.sparta.blogpost.controller;

import com.sparta.blogpost.dto.BlogPostRequestDto;
import com.sparta.blogpost.dto.BlogPostResponseDto;
import com.sparta.blogpost.service.BlogPostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogPosts")
public class BlogPostController {

    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping("")
    public BlogPostResponseDto createBlogPost(@RequestBody BlogPostRequestDto requestDto) {
        return blogPostService.createBlogPost(requestDto);
    }

    @GetMapping("")
    public List<BlogPostResponseDto> getBlogPosts() {
        return blogPostService.getBlogPosts();
    }

    @PutMapping("/{id}")
    public Long updateBlogPost(@PathVariable Long id, @RequestBody BlogPostRequestDto requestDto) {
        return blogPostService.updateBlogPost(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public Long deleteBlogPost(@PathVariable Long id) {
        return blogPostService.deleteBlogPost(id);
    }

    @GetMapping("/contents")
    public List<BlogPostResponseDto> getBlogPostsByKeyword(String keyword){
        return blogPostService.getBlogPostsByKeyword(keyword);
    }
}