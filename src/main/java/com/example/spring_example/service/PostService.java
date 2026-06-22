package com.example.spring_example.service;

import com.example.spring_example.entity.Post;
import com.example.spring_example.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // 게시글 전체 조회(페이징)
    public Page<Post> getPagedPosts(int page, int size){        // 반환해라, page번째 페이지의 게시글 size개를
                Pageable pageable = PageRequest.of(page, size, Sort.by("id")
                        .descending());     //  만들어라, 페이지 요청 설정을, page번째, size개씩, id 내림차순으로
        return postRepository.findAll(pageable);        // 반환해, postRepository에서 pageable 설정대로 조회한 게시글 목록을
    }

    // 게시글 작성
    public Post createPost(Post post){
        return postRepository.save(post);           // Repository 의 save() 호출
    }

//    // 게시글 단건(1개) 조회
//    public Post getPost(Long id){       // Long 타입의 PK 로 설정한 id 를 post 에서 가져와
//        return postRepository.findById(id)      // postRepository 에 있는 id 를 반환해
//                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다. id : "+id));   // () -> 만약 값이 없으면 화살표 뒤에 예외를 던져
//    }

    //게시글 단건 조회 및 조회수 증가
    public Post getPost(Long id){       // Long 타입의 PK 로 설정한 id 를 post 에서 가져와
        Post post = postRepository.findById(id)     //  찾아라, postRepository에서, id에 해당하는 게시글을
                .orElseThrow(()->new RuntimeException("게시글이 존재하지 않습니다. id : "+id));     // 없으면 에러를 던져
        post.setViewCount(post.getViewCount()+1);       // 바꿔라, post의 조회수를, 현재 조회수에 1을 더한 값으로
        return postRepository.save(post);       // 반환해라, postRepository에 저장한 post를.
    }

    // 게시글 수정
    public Post updatePost(Long id, Post updatedPost){  // Post를 돌려주는 updatePost 라는 메서드인데, id랑 updatedPost(새로운 게시글)를 입력값으로 받는다
        Post post = postRepository.findById(id)     // postRepository(DB 창구)한테 .findById(id)(이 번호로 게시글 찾아줘)라고 시킨다. 찾은 결과를 post(기존 게시글을 담을 그릇)에 담는다
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다. id : "+id));   // 만약 못 찾으면 .orElseThrow(...)(에러를 던져라)가 실행된다

        post.setTitle(updatedPost.getTitle());  // updatedPost(새 데이터)에서 제목을 꺼내서, post(기존 게시글)의 제목 자리에 덮어쓴다
        post.setContent(updatedPost.getContent());  // updatedPost(새 데이터)에서 내용을 꺼내서, post(기존 게시글)의 내용 자리에 덮어쓴다
        post.setUserName(updatedPost.getUserName());    // updatedPost(새 데이터)에서 작성자를 꺼내서, post(기존 게시글)의 작성자 자리에 덮어쓴다

        return postRepository.save(post);   // 내용이 바뀐 **post**를 postRepository(DB 창구)한테 건네서 .save(...)(저장해줘)라고 시킨다. 이 post는 이미 id가 있는 상태라서, DB에는 새로 추가(INSERT)가 아니라 기존 걸 덮어쓰는(UPDATE)로 처리된다. 저장된 결과를 그대로 돌려준다
    }

    // 게시글 삭제
    public void deletePost(Long id){    // 반환 값이 없는(void) deletePost 메스드에 삭제할 게시글 id를 받는다
        postRepository.deleteById(id);  // Repository에서 id가 같은 게시글을 삭제(deleteById) 하라고 시킴
    }
}
