package com.sparta.post.entity;

import com.sparta.post.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Table(name = "post") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //setAuthor
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @Column(nullable = false)
    private Integer postLikeCount;

    public Post(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.postLikeCount = 0; // 기본값 설정

    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void addCommentList(Comment comment) {
        this.commentList.add(0, comment);
    }

    public Integer getPostLikeCount() {
        return postLikeCount;
    }

//    public void setPostLikeCount(Integer postLikeCount) {
//        this.postLikeCount = postLikeCount;
//    }

//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setContents(String contents) {
//        this.contents = contents;
//    }
}