package com.sparta.blogpost.service;

import com.sparta.blogpost.dto.BlogPostRequestDto;
import com.sparta.blogpost.dto.BlogPostResponseDto;
import com.sparta.blogpost.entity.BlogPost;
import com.sparta.blogpost.repository.BlogPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BlogPostService {
    private final BlogPostRepository blogPostRepository;

    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public BlogPostResponseDto createBlogPost(BlogPostRequestDto requestDto) {
        // RequestDto -> Entity
        BlogPost blogPost = new BlogPost(requestDto);
        // DB 저장
        BlogPost saveBlogPost = blogPostRepository.save(blogPost);
        // Entity -> ResponseDto
        return new BlogPostResponseDto(saveBlogPost);
    }

    public List<BlogPostResponseDto> getBlogPosts() {
        // DB 조회
        return blogPostRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogPostResponseDto::new).toList();
    }

    public BlogPostResponseDto getBlogPost(Long id) {
        BlogPost blogPost = findBlogPost(id);
        return new BlogPostResponseDto(blogPost);
    }
    public List<BlogPostResponseDto> getBlogPostsByKeyword(String keyword) {
        return blogPostRepository.findAllByContentsContainsOrderByModifiedAtDesc(keyword).stream().map(BlogPostResponseDto::new).toList();
    }

    @Transactional
    public BlogPostResponseDto updateBlogPost(Long id, @RequestBody BlogPostRequestDto blogPostRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        BlogPost blogPost = findBlogPost(id);
        // 비밀번호 체크
        blogPost.checkPassword(blogPostRequestDto.getPassword());
            // post 내용 수정
            blogPost.update(blogPostRequestDto);
            return new BlogPostResponseDto(blogPost);
        }

    @Transactional
    public void deleteBlogPost(Long id, String password) {
        // 해당 메모가 DB에 존재하는지 확인
        BlogPost blogPost = findBlogPost(id);
        blogPost.checkPassword(password);
            // post 삭제
            blogPostRepository.delete(blogPost);
    }

    private BlogPost findBlogPost(Long id) {
        return blogPostRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
    }
}
