package com.sparta.post.repository;

import com.sparta.post.entity.CommentLikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeInfoRepository extends JpaRepository<CommentLikeInfo, Long> {
    Integer countByCommentIdAndIsLikeIsTrue(Long commentId);

    Optional<CommentLikeInfo> findByCommentIdAndUsername(Long commentId, String username);
}
