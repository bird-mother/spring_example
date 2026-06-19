package com.example.spring_example.service;

import com.example.spring_example.entity.Post;
import com.example.spring_example.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service    // 이 클래스가 service 계층이라고 Spring에게 전달. Controller 에서 가져다 쓸 수 있게 해주는 표시
@RequiredArgsConstructor    // final 필드를 매개변수로 받는 생성자 자동 생성
//클래스 안에 선언한 final 필드를 자동으로 생성해주는 생성자를 lombok 이 만들어줌
//만약 집적 작성하게 된다고 하면 아래 코드와 같은데, 이것을 생략하게 해주는 어노테이션
//public PostService(PostRepository postRepository) {
//    this.postRepository = postRepository;
//}
public class PostService {

    private final PostRepository postRepository;    // Repository 를 주입함
    // final 로 생성하는 이유는, 한번 주입받고 수정되지 않도록 고정하기 위해서 이다.
    // Spring이 처음 객체 생성할 때 딱 한번 주입해주고 끝

    // 게시글 전체 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();            // Repository 의 findAll() 호출
    }
//    Repository 의 메서드를 그대로 가져다 쓰는 부분이다
//    Service는 Repository를 호출만 하고 실제 DB 처리는 JPA가 처리한다

    // 게시글 작성
    public Post createPost(Post post){
        return postRepository.save(post);           // Repository 의 save() 호출
    }

    // 게시글 단건(1개) 조회
    public Post getPost(Long id){       // Long 타입의 PK 로 설정한 id 를 post 에서 가져와
        return postRepository.findById(id)      // postRepository 에 있는 id 를 반환해
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다. id : "+id));   // () -> 만약 값이 없으면 화살표 뒤에 예외를 던져
    }
}
