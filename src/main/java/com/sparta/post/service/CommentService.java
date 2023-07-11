package com.sparta.post.service;

import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.entity.Comment;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.User;
import com.sparta.post.entity.UserRoleEnum;
import com.sparta.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

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
}
