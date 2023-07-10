package com.sparta.post.service;

import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.entity.*;
import com.sparta.post.repository.CommentRepository;
import com.sparta.post.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sparta.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(com.sparta.post.dto.CommentRequestDto requestDto, com.sparta.post.entity.User user) {
        //양방향일 때
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        com.sparta.post.entity.Comment comment = new com.sparta.post.entity.Comment(post, requestDto.getContents(), user);
        post.addCommentList(comment);
        log.info("댓글 " + comment.getContents() + " 등록");
        return new CommentResponseDto(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(com.sparta.post.entity.User user, CommentRequestDto requestDto) {
        com.sparta.post.entity.Comment comment = findComment(requestDto.getCommentId());

        if (!user.getRole().equals(com.sparta.post.entity.UserRoleEnum.ADMIN)&&!comment.getUser().equals(user)) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(com.sparta.post.entity.User user, CommentRequestDto requestDto) {

        com.sparta.post.entity.Comment comment = findComment(requestDto.getCommentId());

        if (!user.getRole().equals(com.sparta.post.entity.UserRoleEnum.ADMIN)&&!comment.getUser().equals(user)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        comment.update(requestDto.getContents());

        return new CommentResponseDto(comment);
    }

    public CommentResponseDto getCommentById(Long Id) {
        com.sparta.post.entity.Comment comment = findComment(Id);

        return new CommentResponseDto(comment);
    }
    @Transactional
    public com.sparta.post.entity.Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 id 입니다. 다시 입력해 주세요")
        );
    }
}
