package com.sparta.post.service;

import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.User;

import java.util.List;

public interface PostService {
    /*
    게시글 생성
    게시글 정보
    유저 정보
     */
    PostResponseDto createPost(PostRequestDto postRequestDto, User user);

    /*
    게시글 전체 리스트 조회
     */
    List<PostResponseDto> getPosts();

    /*
    선택한 게시글 조회
    선택한 게시글 번호
     */
    PostResponseDto getPost(Long id);

    /*
    게시글 수정
    기존 게시글 정보
    수정할 게시글 정보
    유저 정보
     */
    PostResponseDto updatePost(Post post, PostRequestDto postRequestDto, User user);

    /*
    게시글 삭제
    게시글 정보
    유저 정보
     */
    void deletePost(Post post, User user);

    /*
    게시글 좋아요
    게시글 번호
    유저 정보
     */
    ApiResponseDto likePost(Long id, User user);

    /*
    게시글 좋아요 취소
    게시글 번호
    유저 정보
     */
    ApiResponseDto unLikePost(Long id, User user);

    /*
    게시글 찾기
    게시글 번호
     */
    Post findPost(Long id);
}
