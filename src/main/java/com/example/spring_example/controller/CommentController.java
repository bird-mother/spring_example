package com.example.spring_example.controller;

import com.example.spring_example.entity.Comment;
import com.example.spring_example.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController     // 이 어노테이션이 없으면 Spring이 컨트롤러로 인식하지 못함
@RequestMapping("/api/posts/{postId}/comments")     // 이 컨트롤러의 기본 주소를 /api/posts/{postId}/comments로 지정
@RequiredArgsConstructor        // final 로 선언된 필드를 매개변수로 받는 생성자를 lombok이 자동으로 만들어줌
public class CommentController {
    private final CommentService commentService;        // CommentService(댓글 서비스) 타입을 담는 commentService 변수를 선언

    // 댓글 작성
    @PostMapping        // @PostMapping는 POST 방식으로 요청이 오면 실행
    public Comment createComment(@PathVariable Long postId, @RequestBody Comment comment){      // 소의 {postId}를 꺼내서 postId 변수에 담고, 요청 본문 JSON을 꺼내서 comment 변수에 담음
        return commentService.createComment(postId, comment);       // 반환해. commentService가 postId와 comment로 만든 댓글을.
    }

    // 댓글 조회
    @GetMapping     //GET 방식으로 요청이 오면 실행
    public List<Comment> getComments(@PathVariable Long postId){        // 받아라, GET 요청을. 꺼내라, 주소에서 postId를.
        return commentService.getComments(postId);      //  반환해라, commentService가 postId로 찾은 댓글 목록을
    }

    // 댓글 수정
    @PutMapping("/{commentId}")     // PUT 방식으로 /{commentId} 주소로 요청이 오면 실행
    public Comment updateComment(@PathVariable Long commentId, @RequestBody Comment comment){   // 받아라, PUT 요청을. 꺼내라, 주소에서 commentId와 본문에서 comment를
        return commentService.updateComment(commentId, comment);        // 반환해라, commentService가 commentId로 찾아서 comment로 수정한 댓글을.
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")      // DELETE 방식으로 /{commentId} 주소로 요청이 오면 실행
    public void deleteComment(@PathVariable Long commentId){        // 받아라, DELETE 요청을. 꺼내라, 주소에서 commentId를.
        commentService.deleteComment(commentId);        // 실행해라, commentService의 삭제 기능을, commentId 번호의 댓글로.
    }
}