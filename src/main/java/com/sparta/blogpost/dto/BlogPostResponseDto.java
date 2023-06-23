package com.sparta.blogpost.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.blogpost.entity.BlogPost;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogPostResponseDto {
    private String status;
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BlogPostResponseDto(BlogPost blogPost) {
        this.id = blogPost.getId();
        this.title = blogPost.getTitle();
        this.username = blogPost.getUsername();
        this.contents = blogPost.getContents();
        this.createdAt = blogPost.getCreatedAt();
        this.modifiedAt = blogPost.getModifiedAt();
    }

    public BlogPostResponseDto(String status) {
        this.status = status;
    }
}