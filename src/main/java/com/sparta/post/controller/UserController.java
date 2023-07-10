package com.sparta.post.controller;


import com.sparta.post.dto.*;
import com.sparta.post.jwt.JwtUtil;
import com.sparta.post.security.UserDetailsImpl;
import com.sparta.post.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//@Controller
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;


    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponseDto> signup(@RequestBody AuthRequestDto requestDto) {//클라이언트로부터 SignupRequestDto 를 요청 RequestBody 로 받아와서 처리

        try {
            userService.signup(requestDto); //회원 가입을 처리하기 위해 userService.signup(requestDto)를 호출
        } catch (IllegalArgumentException e) { // 중복된 username 이 있는 경우
            return ResponseEntity.badRequest().body(new ApiResponseDto("이미 존재하는 id 입니다. 다른 id를 입력해 주세요", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.status(201).body(new ApiResponseDto("회원가입 완료 되었습니다.", HttpStatus.CREATED.value()));
    }

    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponseDto> login(@RequestBody AuthRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            userService.login(loginRequestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDto.getUsername(), loginRequestDto.getRole()));

        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.OK.value()));
    }

    // 헤더 값에서 토큰 반환
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
