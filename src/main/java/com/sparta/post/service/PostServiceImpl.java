package com.sparta.post.service;

import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.PostLike;
import com.sparta.post.entity.User;
import com.sparta.post.repository.PostLikeRepository;
import com.sparta.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        // RequestDto -> Entity
        Post post = new Post(postRequestDto);
        post.setUser(user);
        // DB 저장
        postRepository.save(post);
        // Entity -> ResponseDto
        return new PostResponseDto(post);
    }

    @Override
    public List<PostResponseDto> getPosts() {
        // DB 조회
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(post -> new PostResponseDto(post))
                .toList();
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Post post, PostRequestDto postRequestDto, User user) {

        // post 내용 수정
        post.update(postRequestDto);
        return new PostResponseDto(post);
    }

    @Override
    public void deletePost(Post post, User user) {
        postRepository.delete(post);
    }

    // post 좋아요
    @Override
    @Transactional
    public ApiResponseDto likePost(Long id, User user) {
        //자신의 게시글에 좋아요 X
        Post post = findPost(id);

        if (post.getUser().equals(user)) {
            return new ApiResponseDto("자신의 게시글에는 '좋아요'를 할 수 없습니다.", 400);
        }
        PostLike postLike = postLikeRepository.findByPostIdAndUser(id, user).orElse(null);

        if (postLike == null) {
            postLike = new PostLike(post, user);
            postLike.setLike(true);
            postLikeRepository.save(postLike);
            return new ApiResponseDto("좋아요", 200);
        } else if (!postLike.getIsLike()){
            postLike.setLike(true);
            postLikeRepository.save(postLike);
            return new ApiResponseDto("좋아요", 200);
        } else {
            throw new IllegalArgumentException("이미 좋아요 한 게시물입니다.");
        }
    }

    @Override
    public ApiResponseDto unLikePost(Long id, User user) {
        PostLike postLike = postLikeRepository.findByPostIdAndUser(id, user).orElseThrow();
        if (postLike.getIsLike()) {
            postLike.setLike(false);
            postLikeRepository.save(postLike);
            return new ApiResponseDto("좋아요 취소", 200);
        } else {
            return new ApiResponseDto("취소할 좋아요가 없습니다.",400);
        }
    }

    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
    }
}
