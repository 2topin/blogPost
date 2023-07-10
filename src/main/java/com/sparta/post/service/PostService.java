package com.sparta.post.service;

import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.User;
import com.sparta.post.entity.UserRoleEnum;
import com.sparta.post.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Post post = new Post(requestDto);
        post.setUsername(user.getUsername());
        // DB 저장
        Post savePost = postRepository.save(post);
        // Entity -> ResponseDto
        return new PostResponseDto(savePost);
    }

    public List<PostResponseDto> getPosts() {
        // DB 조회
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }
    public List<PostResponseDto> getPostsByKeyword(String keyword) {
        return postRepository.findAllByContentsContainsOrderByModifiedAtDesc(keyword).stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, @RequestBody PostRequestDto postRequestDto, User user) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        // 작성자이거나 관리자인지 체크
        if(!post.getUsername().equals(user.getUsername()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }
            // post 내용 수정
            post.update(postRequestDto);
            return new PostResponseDto(post);
        }

    @Transactional
    public String deletePost(Long id, User user) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 작성자이거나 관리자인지 체크
        if(!post.getUsername().equals(user.getUsername()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new AccessDeniedException("삭제할 권한이 없습니다.");
    }
        // post 삭제
        postRepository.delete(post);
        return "게시글이 성공적으로 삭제되었습니다.";
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글이 존재하지 않습니다.");
    }
}
