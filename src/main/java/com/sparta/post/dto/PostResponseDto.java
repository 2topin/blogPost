package com.sparta.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.post.entity.Comment;
import com.sparta.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto extends ApiResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer postLikeCount;
    private List<CommentResponseDto> commentList;



    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.postLikeCount = post.getPostLikeCount();
        this.commentList = post.getCommentList().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt)
                        .reversed()).map(CommentResponseDto::new).toList();
    }
}