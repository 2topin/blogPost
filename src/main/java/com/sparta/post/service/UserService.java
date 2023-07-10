package com.sparta.post.service;

import com.sparta.post.dto.AuthRequestDto;
import com.sparta.post.dto.UserResponseDto;
import com.sparta.post.dto.UserUpdateRequestDto;
import com.sparta.post.entity.User;
import com.sparta.post.entity.UserRoleEnum;

import com.sparta.post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.RejectedExecutionException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    // BlackList 를 저장할 Repository
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //로그인
    public void login(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsernameAndIsConfirmIsTrue(username).orElseThrow(
                () -> new IllegalArgumentException(" 등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void signup(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 id 입니다. 다른 id를 입력해 주세요");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    //id에 해당하는 유저 조희
    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
    }
}