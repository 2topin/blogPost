package com.sparta.post.entity;

import com.sparta.post.entity.Post;
import com.sparta.post.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    public Post post;

    @Column(nullable = false)
    private Integer commentLikedCount;

    @Column(nullable = false)
    private String username;

    public Comment(Post post, String contents, User user) {
        this.post = post;
        this.contents = contents;
        this.user = user;
        this.username = user.getUsername();
        this.commentLikedCount = 0; // 기본값 설정

    }

    public Integer getCommentLikedCount() {
        return commentLikedCount;
    }

    public void setCommentLikedCount(Integer commentLikedCount) {
        this.commentLikedCount = commentLikedCount;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}