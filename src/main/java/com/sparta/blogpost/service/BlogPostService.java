package com.sparta.blogpost.service;

import com.sparta.blogpost.dto.BlogPostRequestDto;
import com.sparta.blogpost.dto.BlogPostResponseDto;
import com.sparta.blogpost.entity.BlogPost;
import com.sparta.blogpost.repository.BlogPostRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class BlogPostService {
    private BlogPostRepository blogPostRepository;

    public BlogPostService(BlogPostRepository blogPostRepository) {

        this.blogPostRepository = blogPostRepository;
    }
    public BlogPostResponseDto createBlogPost(BlogPostRequestDto requestDto) {

        // RequestDto -> Entity
        BlogPost blogPost = new BlogPost(requestDto);

        // DB 저장

        BlogPost saveBlogPost = blogPostRepository.save(blogPost);

        // Entity -> ResponseDto
        BlogPostResponseDto blogPostResponseDto = new BlogPostResponseDto(blogPost);

        return blogPostResponseDto;
    }

    public List<BlogPostResponseDto> getBlogPosts() {
        // DB 조회
        return blogPostRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogPostResponseDto::new).toList();
    }
    public List<BlogPostResponseDto> getBlogPostsByKeyword(String keyword) {
        return blogPostRepository.findAllByContentsContainsOrderByModifiedAtDesc(keyword).stream().map(BlogPostResponseDto::new).toList();
    }
    @Transactional
    public Long updateBlogPost(Long id, BlogPostRequestDto requestDto) {

        // 해당 메모가 DB에 존재하는지 확인
        BlogPost blogPost = findBlogPost(id);

            // post 내용 수정
            blogPost.update(requestDto);
            return id;
        }
    @Transactional
    public Long deleteBlogPost(Long id) {

        // 해당 메모가 DB에 존재하는지 확인
        BlogPost blogPost = findBlogPost(id);
            // post 삭제
            blogPostRepository.delete(blogPost);

            return id;

    }

    private BlogPost findBlogPost(Long id) {
        return blogPostRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다."));

    }

}
