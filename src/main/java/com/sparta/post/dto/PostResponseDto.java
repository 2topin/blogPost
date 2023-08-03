package com.sparta.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.post.entity.Comment;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.PostLike;
import com.sparta.post.repository.PostLikeRepository;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponseDto extends ApiResponseDto {
    //    private Long id;
    private String title;
    private String contents;
    private String username;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;



    public PostResponseDto( Post post) {
//        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.contents = post.getContents();
        this.likeCount = (int) post.getPostLikes().stream()
                .filter(PostLike::getIsLike)
                .count();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
//        this.postLikeCount = post.getPostLikeCount();
        this.commentList = post.getCommentList().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt)
                        .reversed()).map(CommentResponseDto::new).toList();

    }
}