package com.sparta.post.controller;

import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.User;
import com.sparta.post.security.UserDetailsImpl;
import com.sparta.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, User user) {
        PostResponseDto post = postService.createPost(postRequestDto, user);

        return ResponseEntity.status(201).body(post);
    } // 게시글 작성

    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    } // 전체 조회

    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    } // 하나 조회

    @PutMapping("/posts/{id}")
    public PostResponseDto updatePost(@PathVariable Long id,
                                      @RequestBody PostRequestDto postRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails.getUser());
    } // 수정

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok(msg);
    } // 삭제

//    @GetMapping("/posts/contents")
//    public List<postResponseDto> getPostsByKeyword(String keyword){
//        return postService.getPostsByKeyword(keyword);
//    }
}