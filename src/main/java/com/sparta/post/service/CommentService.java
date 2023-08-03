package com.sparta.post.service;

import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.CommentRequestDto;
import com.sparta.post.dto.CommentResponseDto;
import com.sparta.post.entity.Comment;
import com.sparta.post.entity.User;

public interface CommentService {
    /*
    댓글 생성
    댓글 정보
    유저 정보
     */
    CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user);

    /*
    댓글 수정
    기존 댓글
    유저 정보
    수정된 댓글 정보
     */
    CommentResponseDto updateComment(Comment comment, User user, CommentRequestDto commentRequestDto);

    /*
    댓글 삭제
    댓글 정보
    유저 정보
     */
    void deleteComment(Comment comment, User user);

    /*
    댓글 좋아요
    댓글 번호
    유저 정보
     */
    ApiResponseDto likeComment(Long id, User user);

    /*
    댓글 좋아요 취소
    댓글 번호
    유저 정보
     */
    ApiResponseDto unLikeComment(Long id, User user);

    /*
    댓글 찾기
    댓글 번호
     */
    Comment findComment(Long id);
}
