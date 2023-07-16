package com.sparta.post.dto;

import com.sparta.post.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto extends ApiResponseDto{
    private Long id;
    private String contents;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer commentLikedCount;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.contents=comment.getContents();
        this.username=comment.getUser().getUsername();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.commentLikedCount = comment.getCommentLikedCount();

    }
}