package com.sparta.blogpost.entity;

import com.sparta.blogpost.dto.BlogPostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "blogpost") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class BlogPost extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @Column(name = "password", nullable = false)
    private String password;

    public BlogPost(BlogPostRequestDto blogPostRequestDto) {
        this.title = blogPostRequestDto.getTitle();
        this.password = blogPostRequestDto.getPassword();
        this.username = blogPostRequestDto.getUsername();
        this.contents = blogPostRequestDto.getContents();
    }

    public void update(BlogPostRequestDto blogPostRequestDto) {
        this.title = blogPostRequestDto.getTitle();
        this.password = blogPostRequestDto.getPassword();
        this.username = blogPostRequestDto.getUsername();
        this.contents = blogPostRequestDto.getContents();
    }

    public void checkPassword(String inputPassword) {
        if (!password.equals(inputPassword)) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
    }
}