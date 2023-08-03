package com.sparta.post.repository;

import com.sparta.post.entity.CommentLike;
import com.sparta.post.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Integer countByCommentIdAndIsLikeIsTrue(Long commentId);

    Optional<CommentLike> findByCommentIdAndUser(Long id, User user);
}
