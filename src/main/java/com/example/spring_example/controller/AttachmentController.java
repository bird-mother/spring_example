package com.example.spring_example.controller;

import com.example.spring_example.entity.Attachment;
import com.example.spring_example.entity.Post;
import com.example.spring_example.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController     // 이 클래스가 JSON으로 응답하는 컨트롤러임을 spring에게 알림
@RequestMapping("/api/attachments")     // 이 컨트롤러의 기본 주소를 지정
@RequiredArgsConstructor        // final 로 건언된 필드를 매개변수로 받는 생성자를 만듬
public class AttachmentController {
    // 매개변수 선언
    // 파일 첨부하는 서비스를 담는 변수 선언
    private final AttachmentService attachmentService;

    // 게시글 파일 업로드
    @PostMapping("/post/{postId}")      // 이 주소로 요청이 들어오면 아래 메서드 실행
    public Attachment uploadToPost(     // 만들어, Attachment를 돌려주는 uploadToPost 메서드를
            @PathVariable Long postId, @RequestParam("file")MultipartFile file      // 주소에서 postId를 꺼내, 요청에서 file 이름의 파일을 꺼내 file 변수에 담아
    ) throws IOException {  // throws IOException은 파일 저장 중 오류가 날 수 있어서 반드시 선언해야 함.
        return attachmentService.uploadToPost(postId, file);    // attachmentService한테 postId와 file을 넘겨서 파일 업로드를 시키고, 결과를 그대로 돌려줌
    }

    // 댓글 파일 업로드
    @PostMapping("/comments/{commentId}")
    public Attachment uploadToComment(
            @PathVariable Long commentId, @RequestParam("file")MultipartFile file
    ) throws IOException{
        return attachmentService.uploadToComment(commentId, file);
    }

    // 첨부 파일 조회(게시글)
    @GetMapping("/post/{postId}")       // GET 방식으로 주소 요청이 오면 아래 메서드 실행
    // Attachment 를 담은 List 를 돌려주는 getAttachmentsByPost 메서드임
    public List<Attachment> getAttachmentsByPost(
            @PathVariable Long postId){     // 주소의 postId 꺼내 postId 에 변수에 담음
        return attachmentService.getAttachmentsByPost(postId);      // attachmentService 한테 postId로 찾은 첨부파일 목록 조회 요청하고, 결과 리스트를 그대로 돌려줌
    }

    // 첨부 파일 조회(댓글)
    @GetMapping("/comments/{commentId}")
    public List<Attachment> getAttachmentsByComment(
            @PathVariable Long commentId){
        return attachmentService.getAttachmentsByComment(commentId);
    }

    // 첨부 파일 삭제
    @DeleteMapping("/{attachmenId}")    // DELETE 방식의 주소 요청이 오면 이 메서드 실행
    public void deleteAttachment(@PathVariable Long attachmenId){       // 주소에 id를 꺼내 void 타입의 delete메서드를 만들어
        attachmentService.deleteAttachment(attachmenId);    // 서비스에 있는 삭제기능을 id번호 첨부파일에 실행
    }
}
