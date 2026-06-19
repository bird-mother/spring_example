package com.example.spring_example.controller;

import com.example.spring_example.entity.Post;
import com.example.spring_example.service.PostService;
import lombok.RequiredArgsConstructor;
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
}
