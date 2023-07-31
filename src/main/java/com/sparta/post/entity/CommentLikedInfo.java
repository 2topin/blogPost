package com.sparta.post.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comment_Like_info")
public class CommentLikeInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_Like_id")
    private Long id;
    private Long commentId;
    private String username;
    private Boolean isLike;

    public CommentLikeInfo() {

    }
    public CommentLikeInfo(Long commentId, String username) {
        this.commentId = commentId;
        this.username = username;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean Like) {
        isLike = Like;
    }
}
