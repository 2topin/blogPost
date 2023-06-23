package com.sparta.blogpost.controller;

import com.sparta.blogpost.dto.BlogPostRequestDto;
import com.sparta.blogpost.dto.BlogPostResponseDto;
import com.sparta.blogpost.service.BlogPostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogPostController {
    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping("/blogPosts")
    public BlogPostResponseDto createBlogPost(@RequestBody BlogPostRequestDto blogPostRequestDto) {
        return blogPostService.createBlogPost(blogPostRequestDto);
    } // 생성

    @GetMapping("/blogPosts")
    public List<BlogPostResponseDto> getBlogPosts() {
        return blogPostService.getBlogPosts();
    } // 함께 조회

    @GetMapping("/blogPosts/{id}")
    public BlogPostResponseDto getBlogPost(@PathVariable Long id) {
        return blogPostService.getBlogPost(id);
    } // 하나 조회

    @PutMapping("/blogPosts/{id}")
    public BlogPostResponseDto updateBlogPost(@PathVariable Long id, @RequestBody BlogPostRequestDto blogPostRequestDto) {
        return blogPostService.updateBlogPost(id, blogPostRequestDto);
    } // 수정

    @DeleteMapping("/blogPosts/{id}")
    public BlogPostResponseDto deleteBlogPost(@PathVariable Long id, @RequestBody BlogPostRequestDto blogPostRequestDto) {
        blogPostService.deleteBlogPost(id, blogPostRequestDto.getPassword());
        return new BlogPostResponseDto(true);
    } // 삭제

//    @GetMapping("/blogPosts/contents")
//    public List<BlogPostResponseDto> getBlogPostsByKeyword(String keyword){
//        return blogPostService.getBlogPostsByKeyword(keyword);
//    }
}