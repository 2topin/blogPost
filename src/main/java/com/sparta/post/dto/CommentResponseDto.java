package com.sparta.post.dto;

import com.sparta.post.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto extends ApiResponseDto{
    private Long id;
    private String contents;
    private String body;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents=comment.getBody();
        this.username=comment.getUser().getUsername();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}