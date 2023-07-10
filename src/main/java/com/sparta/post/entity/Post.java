package com.sparta.post.entity;

import com.sparta.post.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "post") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Post extends Timestamped {
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

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    public Post(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.password = postRequestDto.getPassword();
        this.username = postRequestDto.getUsername();
        this.contents = postRequestDto.getContents();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void addCommentList(Comment comment) {
        this.commentList.add(0,comment);
    }
}