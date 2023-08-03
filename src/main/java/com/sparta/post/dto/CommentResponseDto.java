package com.sparta.post.dto;

import com.sparta.post.entity.Comment;
import com.sparta.post.entity.CommentLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto extends ApiResponseDto{
//    private Long id;
    private String contents;
    private String username;
    private Integer likeCount;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
//    private Integer commentLikeCount;

    public CommentResponseDto(Comment comment) {
//        this.id = comment.getId();
        this.contents=comment.getContents();
        this.username=comment.getUser().getUsername();
        this.likeCount = (int) comment.getCommentLikes().stream()
                .filter(CommentLike::getIsLike)
                .count();
        this.createdAt=comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
//        this.commentLikeCount = comment.getCommentLikeCount();
    }
}