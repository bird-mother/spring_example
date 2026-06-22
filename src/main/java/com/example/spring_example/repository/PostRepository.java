package com.example.spring_example.repository;

import com.example.spring_example.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // JpaRepository<엔티티 클래스, PK 타입>
    // 상속을 받는 것 만으로도 기본 CRUD 메서드를 자동으로 제공해줌
    // Spring Data JPA가 앱 실행 시점에 구현체를 자동으로 생성해줌
    // JpaRepository에 정의된 메서드들을 실제 SQL로 변환해 실행
    // save(), findById(), findAll(), deleteById() 등
    // repository는 직접 controller에서 사용 하지 않고,
    // sservice에서 호출하는것이 정석이다.
    // Controller → Service → Repository → DB

    List<Post>findByTitleContaining(String keyword);    // post 중에 title에 keyword가 포함된 게시글을 찾아
}
