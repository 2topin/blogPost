package com.sparta.blogpost.dto;

import com.sparta.blogpost.entity.BlogPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BlogPostResponseDto {
    private Long id;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BlogPostResponseDto(BlogPost blogPost) {
        this.id = blogPost.getId();
        this.username = blogPost.getUsername();
        this.contents = blogPost.getContents();
        this.createdAt = blogPost.getCreatedAt();
        this.modifiedAt = blogPost.getModifiedAt();
    }
}