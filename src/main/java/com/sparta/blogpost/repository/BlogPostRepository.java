package com.sparta.blogpost.repository;

import com.sparta.blogpost.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//SimpleJpaRepository에 @Repository달려있어서 여기는 없어도 됨
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findAllByOrderByModifiedAtDesc();
    List<BlogPost> findAllByContentsContainsOrderByModifiedAtDesc(String keyword);
}
