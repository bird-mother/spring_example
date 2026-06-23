package com.example.spring_example.controller;

import com.example.spring_example.entity.Attachment;
import com.example.spring_example.repository.AttachmentRepository;
import com.example.spring_example.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController     // 이 클래스가 JSON으로 응답하는 컨트롤러임을 spring에게 알림
@RequestMapping("/api/attachments")     // 이 컨트롤러의 기본 주소를 지정
@RequiredArgsConstructor        // final 로 건언된 필드를 매개변수로 받는 생성자를 만듬
public class AttachmentController {
    // 매개변수 선언
    // 파일 첨부하는 서비스를 담는 변수 선언
    private final AttachmentService attachmentService;
    private final AttachmentRepository attachmentRepository;

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

    @Value("${file.upload.path}")
    // application.properties의 file.upload.path 값을 uploadPath 변수에 주입받음
    private String uploadPath;

    // Quill 에디터용 이미지 업로드 - 업로드 후 이미지 URL 반환
    @PostMapping("/image")
    // POST /api/attachments/image 요청이 오면 이 메서드를 실행
    public Map<String, String> uploadImage(
            @RequestParam("file") MultipartFile file) throws IOException {
        // AttachmentService한테 이미지 업로드를 시키고 결과를 attachment에 담음
        Attachment attachment = attachmentService.uploadImage(file);

        // 반환할 URL을 담을 Map 생성
        Map<String, String> result = new HashMap<>();

        // 업로드된 이미지에 접근할 수 있는 URL을 result에 담음
        result.put("url", "/api/attachments/image/" + attachment.getStoredFileName());

        // URL이 담긴 result를 JSON 형태로 반환
        return result;
    }

    // 업로드된 이미지 파일 반환
    @GetMapping("/image/{fileName}")
    // GET /api/attachments/image/{fileName} 요청이 오면 이 메서드를 실행
    public ResponseEntity<Resource> getImage(
            @PathVariable String fileName) throws IOException {
        // uploadPath + fileName으로 파일 경로를 만들어서 Path 객체에 담음
        Path filePath = Paths.get(uploadPath + fileName);

        // 파일 경로로 Resource 객체를 만들어서 파일을 읽을 수 있게 함
        Resource resource = new FileSystemResource(filePath);

        // 파일을 응답으로 반환. Content-Type은 파일 타입에 맞게 자동으로 설정
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                .body(resource);
    }

    // 파일 다운로드
    @GetMapping("/download/{id}")
    // GET /api/attachments/download/{id} 요청이 오면 이 메서드를 실행
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        // attachmentRepository한테 id로 첨부파일을 찾아달라고 시킴. 없으면 에러를 던짐
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("첨부파일이 존재하지 않습니다. id : " + id));

        // 파일 경로로 Path 객체를 만들어서 filePath 변수에 담음
        Path filePath = Paths.get(attachment.getFilePath());

        // 파일 경로로 Resource 객체를 만들어서 파일을 읽을 수 있게 함
        Resource resource = new FileSystemResource(filePath);

        // 파일을 다운로드 형태로 응답. Content-Disposition 헤더로 파일명을 지정
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getOriginalFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                .body(resource);
    }

}
