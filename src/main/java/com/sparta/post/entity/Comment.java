package com.sparta.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    public Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String contents;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();

//    @Column(nullable = false)
//    private Integer commentLikeCount;

    public Comment(Post post, String contents, User user) {
        this.post = post;
        this.contents = contents;
        this.user = user;
//        this.commentLikeCount = 0; // 기본값 설정
    }

//    public Integer getCommentLikeCount() {
//        return commentLikeCount;
//    }
//
//    public void setCommentLikeCount(Integer commentLikeCount) {
//        this.commentLikeCount = commentLikeCount;
//    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}