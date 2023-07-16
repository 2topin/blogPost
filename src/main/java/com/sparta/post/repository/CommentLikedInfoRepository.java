package com.sparta.post.repository;

import com.sparta.post.entity.CommentLikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikedInfoRepository extends JpaRepository<CommentLikedInfo, Long> {
    Integer countByCommentIdAndIsLikedIsTrue(Long commentId);

    Optional<CommentLikedInfo> findByCommentIdAndUsername(Long commentId, String username);
}
