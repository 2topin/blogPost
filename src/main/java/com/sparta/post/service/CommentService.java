package com.sparta.post.service;

import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.entity.*;
import com.sparta.post.repository.CommentRepository;
import com.sparta.post.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sparta.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Post post = postService.findPost(requestDto.getPostId());
        Comment comment = new Comment(requestDto.getBody());
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

        comment.setBody(requestDto.getBody());

        return new CommentResponseDto(comment);
    }

    public CommentResponseDto getCommentById(Long Id) {
        Comment comment = findComment(Id);

        return new CommentResponseDto(comment);
    }
    @Transactional
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 id 입니다. 다시 입력해 주세요")
        );
    }
}
