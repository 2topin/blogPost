package com.sparta.blogpost.dto;

import lombok.Getter;

@Getter
public class BlogPostRequestDto {
    private String title;
    private String username;
    private String contents;
    private String password;
}