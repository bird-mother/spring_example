// 댓글 기능을 담당하는 엔티티
package com.example.spring_example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity     // 이 클래스가 DB와 연결되는 엔티티라고 선언함
@Getter     // 모든 필드에 대해 '읽기 메서드' 를 자동으로 만들어줌
@Setter     // 모든 필드에 대해 '쓰기 메서드' 를 자동으로 만들어줌
@Table(name = "comment")   // DB에 만들어질 테이블 이름 comment 로 지정
public class Comment {      // 외부에서 접근 가능한(public) Comment 클래스 선언
    @Id     // 바로 아래 필드가 PK 라고 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 값(ID)을 자동 생성함 (숫자 형식)
    private Long id;        // 큰 숫자 타입을 담을 수 있는 id 변수 선언

    @Column(nullable = false)       // null값을 허용 하지 않음
    private String content;     // 문자형 타입의 content 컬럼

    @Column(nullable = false)       // null값을 허용 하지 않음
    private String userName;     // 문자형 타입의 userName 컬럼

    @ManyToOne      // 이 클래스가 다른 클래스와 N:1 관계임을 선언 (게시글 1개에 댓글 여러개)
    @JoinColumn(name = "post_id", nullable = false)     // BD에 post_id 라는 컬럼을 만들어서, 어느 게시글의 댓길인지 연결하고 null을 허용하지 않음(댓글은 게시글에 반드시 속해야함)
    private Post post;      // 게시글 타입을 담는 변수 선언(게시글 자체 객체를 담음)

    @OneToMany(mappedBy = "comment", fetch = FetchType.EAGER)
    // comment와 attachment는 1:N 관계. comment 조회 시 attachment도 함께 조회
    private List<Attachment> attachments = new ArrayList<>();
}
