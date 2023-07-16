package com.sparta.post.controller;

import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.security.UserDetailsImpl;
import com.sparta.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommentResponseDto comment = commentService.createComment(commentRequestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponseDto> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            commentService.deleteComment(userDetails.getUser(), id);
            return ResponseEntity.ok().body(new ApiResponseDto("댓글이 삭제 되었습니다.", HttpStatus.OK.value()));
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 댓글 수정
    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponseDto> updateComment(@AuthenticationPrincipal UserDetailsImpl details,
                                                            @RequestBody CommentRequestDto requestDto,
                                                            @PathVariable Long id) {
        try {
            CommentResponseDto responseDto = commentService.updateComment(id, details.getUser(), requestDto);
            return ResponseEntity.ok().body(responseDto);
        } catch (AccessDeniedException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 좋아요
    @PutMapping("/comments/{id}/like")
    public ResponseEntity<ApiResponseDto> addLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            ApiResponseDto responseDto = commentService.addLikeComment(id, userDetails);
            return ResponseEntity.ok().body(responseDto);
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("자신의 댓글에는 좋아요를 할 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
