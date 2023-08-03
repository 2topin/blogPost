package com.sparta.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment_Like")
public class CommentLike extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_Like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isLike;

    public CommentLike(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public void setLike(Boolean isLike) {
        this.isLike = isLike;
    }
}
