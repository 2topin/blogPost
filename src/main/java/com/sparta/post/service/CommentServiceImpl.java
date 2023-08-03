package com.sparta.post.service;

import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.entity.Comment;
import com.sparta.post.entity.CommentLike;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.User;
import com.sparta.post.repository.CommentLikeRepository;
import com.sparta.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        Post post = postService.findPost(commentRequestDto.getPostId());
        Comment comment = new Comment(post, commentRequestDto.getContents(), user);
        post.addCommentList(comment);
        return new CommentResponseDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(Comment comment, User user, CommentRequestDto commentRequestDto) {

        comment.setContents(commentRequestDto.getContents());
        return new CommentResponseDto(comment);
    }

    @Override
    public void deleteComment(Comment comment, User user) {
        commentRepository.delete(comment);
    }

    // comment 좋아요
    @Override
    @Transactional
    public ApiResponseDto likeComment(Long id, User user) {
        Comment comment = findComment(id);
        //자신의 게시글에 좋아요 X
        if (comment.getUser().equals(user)) {
            return new ApiResponseDto("자신의 댓글에는 '좋아요'를 할 수 없습니다.", 400);
        }
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUser(id, user).orElse(null);

        if (commentLike == null) {
            commentLike = new CommentLike(comment, user);
            commentLike.setLike(true);
            commentLikeRepository.save(commentLike);
            return new ApiResponseDto("좋아요", 200);
        } else if(!commentLike.getIsLike()){
            commentLike.setLike(true);
            commentLikeRepository.save(commentLike);
            return new ApiResponseDto("좋아요", 200);
        } else {
            throw  new IllegalArgumentException("이미 좋아요 한 게시물입니다.");
        }
    }

    @Override
    public ApiResponseDto unLikeComment(Long id, User user) {
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndUser(id, user).orElseThrow();
        if (commentLike.getIsLike()) {
            commentLike.setLike(false);
            commentLikeRepository.save(commentLike);
            return new ApiResponseDto("좋아요 취소", 200);
        } else {
            return new ApiResponseDto("취소할 좋아요가 없습니다.",400);
        }
    }

    @Override
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 댓글이 존재하지 않습니다."));
    }
}
