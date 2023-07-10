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
@Setter
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, updatable = false)
    public Post post;

    @Column(nullable = false)
    private String body;

    public Comment(String body) {
        this.body = body;
    }
    public void setUser(String user) {
        this.body = body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public void setPost(String post) {
        this.body = body;
    }
}