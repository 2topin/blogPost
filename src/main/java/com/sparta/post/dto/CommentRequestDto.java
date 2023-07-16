package com.sparta.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank
    private Long postId;

    @NotBlank
    private String contents;

    //PathVa~ 에 없으면 commentId가 여기 있어야 됨
}