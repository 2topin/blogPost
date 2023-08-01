package com.sparta.post.repository;

import com.sparta.post.entity.Post;
import com.sparta.post.entity.PostLike;
import com.sparta.post.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUser(Long id, User user);

    Integer countByPostIdAndIsLikeIsTrue(Long post);
}
