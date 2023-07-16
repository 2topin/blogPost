package com.sparta.post.repository;

import com.sparta.post.entity.PostLikedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikedInfoRepository extends JpaRepository<PostLikedInfo, Long> {
    Optional<PostLikedInfo> findByPostIdAndUsername(Long postId, String username);

    Integer countByPostIdAndIsLikedIsTrue(Long postId);


}
