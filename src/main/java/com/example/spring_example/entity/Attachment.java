package com.example.spring_example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity     // 이 클래스가 DB와 연결되는 엔티티라고 선언함
@Getter     // 모든 필드에 대해 '읽기 메서드' 를 자동으로 만들어줌
@Setter     // 모든 필드에 대해 '쓰기 메서드' 를 자동으로 만들어줌
@Table(name = "attachment")     // 이 테이블의 이름을 지정
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 원본 파일명
    @Column(nullable = false)
    private String originalFileName;

    // 서버에 저장된 파일명 (중복 방지용)
    @Column(nullable = false)
    private String storedFileName;

    // 파일 저장 경로
    @Column(nullable = false)
    private String filePath;

    // 파일 크기 byte
    @Column(nullable = false)
    private Long fileSize;

    // 게시글 첨부 파일
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    // 댓글 첨부 파일
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
