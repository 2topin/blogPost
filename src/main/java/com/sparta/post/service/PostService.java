package com.sparta.post.service;

import com.sparta.post.dto.ApiResponseDto;
import com.sparta.post.dto.PostRequestDto;
import com.sparta.post.dto.PostResponseDto;
import com.sparta.post.entity.Post;
import com.sparta.post.entity.PostLikedInfo;
import com.sparta.post.entity.User;
import com.sparta.post.entity.UserRoleEnum;
import com.sparta.post.repository.PostLikedInfoRepository;
import com.sparta.post.repository.PostRepository;
import com.sparta.post.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
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
    private final PostLikedInfoRepository postLikedInfoRepository;

    public PostService(PostRepository postRepository, PostLikedInfoRepository postLikedInfoRepository) {
        this.postRepository = postRepository;
        this.postLikedInfoRepository = postLikedInfoRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        // RequestDto -> Entity
        User user = userDetails.getUser();
        Post post = new Post(requestDto, user);
        // DB 저장
        Post savePost = postRepository.save(post);
        // Entity -> ResponseDto
        return new PostResponseDto(savePost);
    }

    public List<PostResponseDto> getPosts() {
        // DB 조회
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .toList();
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
    public void deletePost(Long id, User user) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> {
                    // 게시글이 존재하지 않는 경우 예외 발생
                    throw new RejectedExecutionException();
                });

        // 작성자이거나 관리자인지 체크
        if(!post.getUsername().equals(user.getUsername()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new AccessDeniedException("삭제할 권한이 없습니다.");
    }
        // post 삭제
        postRepository.delete(post);
    }

    Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
    }

    // post 좋아요
    @Transactional
    public ApiResponseDto addLikePost(Long postId, UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        // postId와 username을 이용해서 사용자가 이미 Like를 눌렀는지 확인
//        boolean alreadyLiked = likedInfoRepository.findByPostIdAndUsernameAndStatus(postId, username, "liked").isPresent();

        //자신의 게시글에 좋아요 X
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."));
        if (post.getUser().getUsername().equals(username)) {
            throw new RejectedExecutionException("자신의 게시글에는 '좋아요'를 할 수 없습니다.");
        }
        PostLikedInfo postLikedInfo = postLikedInfoRepository.findByPostIdAndUsername(postId, username).orElse(null);

/*        if (postLikedInfo == null) {
            // 좋아요 요청이 처음일 경우, 새로운 LikedInfo 생성
            postLikedInfo = new PostLikedInfo(postId, username);
            postLikedInfo.setStatus("liked");
        } else {
            if (postLikedInfo.getStatus().equals("canceled")) {
                // 좋아요가 취소된 상태에서 요청 시 status를 "liked"로 변경
                postLikedInfo.setStatus("liked");
            } else {
                // 이미 좋아요를 누른 상태에서 요청 시 status를 "canceled"로 변경
                postLikedInfo.setStatus("canceled");
            }
        }*/
        if (postLikedInfo == null) {
            postLikedInfo = new PostLikedInfo(postId, username);
            postLikedInfo.setLiked(true);
            postLikedInfoRepository.save(postLikedInfo);
            updatePostLikedCount(postId);
            return new ApiResponseDto("좋아요", 200);
        } else {
            postLikedInfo.setLiked(!postLikedInfo.getLiked());
            postLikedInfoRepository.save(postLikedInfo);
            updatePostLikedCount(postId);
            if (postLikedInfo.getLiked()) {
                return new ApiResponseDto("좋아요", 200);
            } else {
                return new ApiResponseDto("좋아요 취소", 200);
            }
        }
    }

    // count한 like 저장해주기
    private void updatePostLikedCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Integer postLikedCount = postLikedInfoRepository.countByPostIdAndIsLikedIsTrue(postId);
        post.setPostLikedCount(postLikedCount);
    }

    public PostResponseDto getPostById(Long Id) {
        Post post = findPost(Id);

        return new PostResponseDto(post);
    }
}
