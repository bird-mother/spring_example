package com.example.spring_example.controller;

import com.example.spring_example.entity.Post;
import com.example.spring_example.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                     //JSON 으로 응답하는 컨트롤러를 선언
@RequestMapping("/api/posts")   // 이 컨트롤러의 기본 url
@RequiredArgsConstructor           // lombok 의 final 필드 생성자 자동 생성
public class PostController {

    private final PostService postService;    // Service 를 주입받음

    // 게시글 전체 조회
    @GetMapping                         // GET  /api/posts
    public List<Post> getAllPosts(){
        return postService.getAllPosts();    // Service 의 getAllPosts() 호출
    }

    // 게시글 페이징 조회
    @GetMapping("/paged")       // GET 방식으로 /api/posts/paged 주소 요청이 오면 이 메서드를 실행
    public Page<Post> getPagedPosts(  // 만들어, Page<Post>를 돌려주는 getPagedPosts라는 메서드를.
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size){       // 꺼내, URL 파라미터에서 page와 size를. 없으면 기본값 0, 10을 써
        return postService.getPagedPosts(page, size);       // 반환해. postService에서 page와 size로 조회한 게시물
    }

    // 게시글 작성
    @PostMapping                        // POST /api/posts
    public Post createPost(@RequestBody Post post){
        return postService.createPost(post);    // Service 의 createPost() 호출
    }

    // 게시글 단건(1개) 조회
    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    // 게시글 수정
    @PutMapping("/{id}")   // PUT 방식으로, /{id}(주소 끝에 숫자가 붙는 형태, 예: /api/posts/1)로 요청이 들어오면 이 메서드를 실행
    public Post updatePost(@PathVariable Long id, @RequestBody Post post){
        // Post(게시글 모양)를 돌려주는 **updatePost**라는 메서드인데, @PathVariable Long id(주소에 붙은 숫자를 꺼내서 id라는 변수에 담고), @RequestBody Post post(요청 본문에 담긴 JSON 데이터를 꺼내서 post라는 변수에 담는다).
    return postService.updatePost(id, post);
        //postService(Service 계층)한테 .updatePost(id, post)(이 id의 게시글을, post 내용으로 수정해줘)라고 시키고, 그 결과를 그대로 돌려준다
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")     // delete 방식을 사용할 때 주소 끝에 id 값이 들어오면, 아래 메서드를 실행
    public void deletePost(@PathVariable long id){  // 반환 값이 없는 deletePost 메서드, 주소에 붙은 id 라는 숫자를 꺼내 변수에 담아
        postService.deletePost(id);     // 서비스에 가서 id값과 똑같은 게시글을 지워
    }

    // 게시글 검색
    @GetMapping("/search")      // GET 방식으로 /api/posts/search 주소로 요청이 오면 이 메서드를 실행해
    public List<Post> searchPost(@RequestParam String keyword){     // post를 list로 돌려주는 searchPost 메서드에서 URL에 붙은 keywird를 꺼내
        return postService.searchPost(keyword);     // 반환해. postService가 keyword로 검색한 게시글 목록을
    }
}
