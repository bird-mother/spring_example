//entity 클래스는 erd에서 작성했던 culumn을 작성하여
//db에 jap가 자동으로 테이블을 생성하도록 하는 클래스다

package com.example.spring_example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity     // 이 클래스가 DB테이블 이라고 선언
@Getter     // lombok을 사용하여 getter 메서드를 자동 생성
@Setter     // lombok을 사용하여 setter 메서드를 자동 생성
@Table(name = "post")      // DB에 생성될 테이블 이름을 정의
public class Post {
    // 게시글 번호
    @Id         // 기본 PK 선언
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 기본 PK 생성할 때 숫자가 자동으로 증가하게 해줌
    private Long id;

    // 게시글 제목
    @Column(nullable = false)     // nullable = false : null 값 허용하지 않음
    private String title;

    // 게시글 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    // content 컬럼을 TEXT 타입으로 지정. Quill 에디터가 HTML 태그째로 저장하기 때문에
    // varchar(255)로는 내용이 잘릴 수 있어서 TEXT 타입으로 변경
    private String content;

    // 작성자 이름
    @Column(nullable = false)
    private String userName;         // 카멜케이스로 작성한 컬럼명은 DB 생성될 때 스네이크 케이스로 변환해 준다.

    // 조회수
    @Column(nullable = false)
    private int viewCount = 0;
}
