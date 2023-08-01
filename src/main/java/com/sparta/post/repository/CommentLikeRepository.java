package com.sparta.post.repository;

import com.sparta.post.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Integer countByCommentIdAndIsLikeIsTrue(Long commentId);

    Optional<CommentLike> findByCommentIdAndUsername(Long commentId, String username);
}
