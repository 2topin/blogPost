package com.sparta.post.service;

import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.entity.*;
import com.sparta.post.repository.CommentLikeRepository;
import com.sparta.post.repository.CommentRepository;
import com.sparta.post.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentLikeRepository commentLikeRepository;


    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Post post = postService.findPost(requestDto.getPostId());
        Comment comment = new Comment(post, requestDto.getContents(), user);
        post.addCommentList(comment);
        return new CommentResponseDto(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(User user, Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();

        if (!user.getRole().equals(com.sparta.post.entity.UserRoleEnum.ADMIN)&&!comment.getUser().equals(user)) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, User user, CommentRequestDto requestDto) {

        Comment comment = commentRepository.findById(id).orElseThrow();

        if (!user.getRole().equals(UserRoleEnum.ADMIN)&&!comment.getUser().equals(user)) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        comment.setContents(requestDto.getContents());

        return new CommentResponseDto(comment);
    }

    // comment 좋아요
    @Transactional
    public ApiResponseDto addLikeComment(Long commentId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        // commentId와 username을 이용해서 사용자가 이미 Like를 눌렀는지 확인

        //자신의 게시글에 좋아요 X
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."));
        if (comment.getUser().getUsername().equals(username)) {
            throw new RejectedExecutionException("자신의 댓글에는 '좋아요'를 할 수 없습니다.");
        }
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUsername(commentId, username).orElse(null);

        if (commentLike == null) {
            commentLike = new CommentLike(commentId, username);
            commentLike.setLike(true);
            commentLikeRepository.save(commentLike);
            updateCommentLikeCount(commentId);
            return new ApiResponseDto("좋아요", 200);
        } else {
            commentLike.setLike(!commentLike.getLike());
            commentLikeRepository.save(commentLike);
            updateCommentLikeCount(commentId);
            if (commentLike.getLike()) {
                return new ApiResponseDto("좋아요", 200);
            } else {
                return new ApiResponseDto("좋아요 취소", 200);
            }
        }
    }

    // count한 like 저장해주기
    private void updateCommentLikeCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Integer commentLikeCount = commentLikeRepository.countByCommentIdAndIsLikeIsTrue(commentId);
        comment.setCommentLikeCount(commentLikeCount);
    }
}
