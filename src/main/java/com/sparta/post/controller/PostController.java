package com.sparta.post.controller;

import com.google.protobuf.Api;
import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.User;
import com.sparta.post.security.UserDetailsImpl;
import com.sparta.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto post = postService.createPost(postRequestDto, userDetails.getUser());

        return ResponseEntity.status(201).body(post);
    }

    // 전체 게시글 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getPosts() {
        List<PostResponseDto> result = postService.getPosts();
        return ResponseEntity.ok().body(result);
    }

    // 선택 게시글 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long id,
                                                     @RequestBody PostRequestDto postRequestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            PostResponseDto result;
            Post post = postService.findPost(id);
            result = postService.updatePost(post, postRequestDto, userDetails.getUser());
            return ResponseEntity.ok().body(result);
        } catch (AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정할 수 있습니다.", 400));

        }
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            Post post = postService.findPost(id);
            postService.deletePost(post, userDetails.getUser());
            return ResponseEntity.ok().body(new ApiResponseDto("게시글이 삭제되었습니다.", 200));
        } catch (AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("삭제할 권한이 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 좋아요
    @PutMapping("/posts/{id}/like")
    public ResponseEntity<ApiResponseDto> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            Post post = postService.findPost(id);
            ApiResponseDto responseDto = postService.likePost(post, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("자신의 게시글에는 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

//    // 좋아요
//    @DeleteMapping("/posts/{id}/like")
//    public ResponseEntity<ApiResponseDto> deleteLikePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
//        try {
//            ApiResponseDto responseDto = postService.deleteLikePost(id, userDetails);
//            return ResponseEntity.ok().body(responseDto);
//        } catch (ResponseStatusException e) {
//            return ResponseEntity.notFound().build();
//        } catch (RejectedExecutionException e) {
//            return ResponseEntity.badRequest().body(new ApiResponseDto("자신의 게시글에는 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
//        }
//    }
}


