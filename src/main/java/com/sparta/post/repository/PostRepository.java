package com.sparta.post.repository;

import com.sparta.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//SimpleJpaRepository에 @Repository달려있어서 여기는 없어도 됨
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findAllByContentsContainsOrderByModifiedAtDesc(String keyword);
}
