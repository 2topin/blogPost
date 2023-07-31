package com.sparta.post.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "post_like")
@Getter
public class PostLike extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Boolean isLike;

    public PostLike() {

    }
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean Like) {
        isLike = Like;
    }
}
