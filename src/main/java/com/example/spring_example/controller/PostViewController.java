package com.example.spring_example.controller;

import com.example.spring_example.entity.Post;
import com.example.spring_example.service.AttachmentService;
import com.example.spring_example.service.CommentService;
import com.example.spring_example.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // 이 클래스가 HTML을 응답하는 컨트롤러임을 Spring에게 알림
@RequestMapping("/posts")   // 이 컨트롤러의 기본 주소를 /posts로 지정
@RequiredArgsConstructor    // final로 선언된 필드를 매개변수로 받는 생성자를 Lombok이 자동으로 만들어줌.
public class PostViewController {

    // PostService(게시글 서비스), CommentService(댓글 서비스) 타입을 담는 변수를 선언
    private final PostService postService;
    private final CommentService commentService;
    private final AttachmentService attachmentService;

    // 게시글 목록 페이지
    @GetMapping     // GET /posts 요청이 오면 실행
    // URL 파라미터에서 page, size, sort, searchType, keyword를 꺼내서 각 변수에 담고, 없으면 기본값을 씀.
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "title") String searchType,
            @RequestParam(defaultValue = "") String keyword,
            Model model     //  Model은 HTML에 데이터를 전달하는 그릇임
            ){
        // postService한테 5가지 조건을 넘겨서 게시글 목록을 가져달라고 시키고, 결과를 posts 변수에 담음
        Page<Post> posts = postService.getPagedPosts(page, size, sort, searchType, keyword);
        // Model에 posts, sort, searchType, keyword 값을 담음. Thymeleaf HTML에서 이 값들을 ${posts}, ${sort} 형태로 꺼내 쓸 수 있음
        model.addAttribute("posts", posts);
        model.addAttribute("sort", sort);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);

        return "posts/list";    // templates/posts/list.html 파일을 찾아서 Thymeleaf로 렌더링
    }

    // 게시글 상세 페이지
    @GetMapping("{id}")
    public String detail(   // HTML 파일 이름을 돌려주는 detail 메서드
            @PathVariable Long id, Model model  // 주소의 {id}를 꺼내서 id 변수에 담고, HTML에 데이터를 전달할 Model 그릇을 받음
    ){
        // postService한테 id로 게시글을 가져달라고 시키고, 그 결과를 Model에 post라는 이름으로 담음
        model.addAttribute("post", postService.getPost(id));

        // commentService한테 id로 댓글 목록을 가져달라고 시키고, 그 결과를 Model에 comments라는 이름으로 담음
        model.addAttribute("comments", commentService.getComments(id));

        // attachmentService한테 id에 해당하는 게시글의 첨부파일 목록을 가져달라고 시키고, 결과를 Model에 attachments라는 이름으로 담음
        model.addAttribute("attachments", attachmentService.getAttachmentsByPost(id));

        // templates/posts/detail.html 파일을 찾아서 렌더링
        return "posts/detail";
    }

    // 게시글 작성 페이지(작성 폼 화면 보여줌)
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "posts/create";
    }

    // 게시글 작성 페이지(폼 데이터 저장)
    // 게시글 작성 처리 - 저장 후 게시글 id를 문자열로 반환
    @PostMapping("/new")
    // POST /posts/new 요청이 오면 이 메서드를 실행
    @ResponseBody
    // 반환값을 HTML이 아닌 텍스트로 응답
    public String create(Post post) {
        // postService한테 post를 저장해달라고 시키고 저장된 post를 반환받음
        Post savedPost = postService.createPost(post);
        // 저장된 게시글의 id를 문자열로 반환
        return String.valueOf(savedPost.getId());
    }

    // 게시글 수정 페이지
    @GetMapping("/{id}/edit")
    public String editForm(     // HTML 파일 이름을 돌려주는 editForm 메서드.
            @PathVariable Long id, Model model      // 주소의 {id}를 꺼내서 id 변수에 담고, Model 그릇을 받음
    ){
        // postService한테 id로 기존 게시글을 가져달라고 시키고, 그 결과를 Model에 post라는 이름으로 담음
        model.addAttribute("post", postService.getPost(id));
        // templates/posts/edit.html 파일을 찾아서 렌더링
        return "posts/edit";
    }
}
