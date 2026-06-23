package com.example.spring_example.service;

import com.example.spring_example.entity.Attachment;
import com.example.spring_example.entity.Comment;
import com.example.spring_example.entity.Post;
import com.example.spring_example.repository.AttachmentRepository;
import com.example.spring_example.repository.CommentRepository;
import com.example.spring_example.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service    // 이 클래스가 Service 계층임을 알림
@RequiredArgsConstructor    // final 로 선언된 필드를 주입받는 생성자를 자동으로 만듬
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;    // 첨부파일을 DB에 저장하고 조회하려면 ATtachmentRepository 타입 변수를 final로 선언하여 바뀌지 않게 고정
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Value("${file.upload.path}")   // properties에 있는 file.upload.path를 가져와
    private String uploadPath;  // @Value 가 주입한 경로 값이 이 변수에 담김

    // 게시글에 파일 업로드
    // 만들어, attachment를 돌려주는 uploadTOPost 메서드를. 받아와 postId와 file
    // MultipartFile 은 클라이언트가 업로드한 파일을 담는 타입
    public Attachment uploadToPost(long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId) // 찾아와 postRepository에서 postId에 해당하는 게시물
                // findById 결과가 없으면(게시물이 없으면) 에러를 던져
                .orElseThrow(()-> new RuntimeException("게시글이 존재하지 않습니다. id : "+postId));
        return saveFile(file, post, null);  // 공통 파일 저장 메서드 saveFile 을 호출함. 반환해 file과 post 로 저장한 결과
    }

    // 댓글에 파일 업로드
    public Attachment uploadToComment(Long commentId, MultipartFile file) throws IOException{
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new RuntimeException("댓글이 존재하지 않습니다. id : "+commentId));
        return saveFile(file, null, comment);
    }

    // 저장 로직
    // Attachment 를 돌려주는 saveFile 메서드, 업로르된 파일, 게시글, 댓글 을 입력 값으로 받음
    private Attachment saveFile(MultipartFile file, Post post, Comment comment) throws IOException{
        // File 선언할때 import java.io 로 선언
        File uploadDir = new File(uploadPath);     // uploadPath(저장 경로)로 File 객체를 만들어서 uploadDir 변수에 담음
        if(!uploadDir.exists()) {       // uploadDir(업로드 폴더)가 존재하지 않으면 아래 코드를 실행
            uploadDir.mkdirs();     // uploadDir 경로의 폴더를 생성함. 중간 경로 폴더도 없으면 함께 생성
        }

        // 업로드된 file에서 원본 파일명을 꺼내서 originalFileName 변수에 담음
        String originalFilename = file.getOriginalFilename();

        // UUID.randomUUID()로 고유한 랜덤 값을 생성하고, 원본 파일명 앞에 붙여서 storedFileName 변수에 담음
        String storedFileName = UUID.randomUUID() + "_" + originalFilename;

        // uploadPath(저장 폴더 경로)와 storedFileName(저장될 파일명)을 합쳐서 filePath 변수에 담음
        String filePath = uploadPath + storedFileName;

        // 업로드된 file을 filePath 경로에 실제로 저장
        file.transferTo(new File(filePath));

        // Attachment 타입의 새 객체를 만들어서 attachment 변수에 담음
        Attachment attachment = new Attachment();

        // attachment의 원본 파일명 자리에 originalFileName 값을 넣음
        attachment.setOriginalFileName(originalFilename);

        // attachment의 저장 파일명 자리에 storedFileName 값을 넣음
        attachment.setStoredFileName(storedFileName);

        // attachment의 파일 경로 자리에 filePath 값을 넣음
        attachment.setFilePath(filePath);

        // 업로드된 file에서 파일 크기를 꺼내서(getSize) attachment의 파일 크기 자리에 넣음
        attachment.setFileSize(file.getSize());

        // attachment의 게시글 자리에 post 객체를 넣음
        attachment.setPost(post);

        // attachment의 댓글 자리에 comment 객체를 넣음
        attachment.setComment(comment);

        // 정보가 채워진 attachment를 attachmentRepository한테 건네서 DB에 저장하고, 저장된 결과를 돌려줌
        return attachmentRepository.save(attachment);
    }

    // 첨부 파일 조회 - 게시글
    // Attachment를 담은 리스트를 가져오는 getAttachmentsByPost는 조회할 게시글 id를 값으로 받음
    public List<Attachment> getAttachmentsByPost(Long postId){
        // attachmentRepository 에서 postId 로 찾은 첨부파일 목록을 반환
        return attachmentRepository.findByPostId(postId);
    }

    // 첨부 파일 조회 - 댓글
    public List<Attachment> getAttachmentsByComment(Long commentId){
        return attachmentRepository.findByCommentId(commentId);
    }

    // 첨부 파일 삭제
    public void deleteAttachment(Long attachmentId){        // 삭제할 첨부 파일 번호를 입력값으로 받고 아무 것도 돌려주지 않음
        Attachment attachment = attachmentRepository.findById(attachmentId)     // attachmentRepository한테 attachmentId로 첨부파일을 찾아달라고 시킴
                // 없으면 에러를 던짐
                .orElseThrow(()-> new RuntimeException("첨부파일이 존재하지 않습니다. id : "+attachmentId));

        File file = new File(attachment.getFilePath());     // attachment에서 파일 경로를 꺼내서(getFilePath) File 객체를 만들어 file 변수에 담음
        if(file.exists()){      // 로컬 폴더에 file 이 존재하는지 확인하고 있으면
            file.delete();      // 로컬 폴더에 있는 file 을 삭제
        }
        // attachmentRepository한테 attachmentId 번호의 첨부파일 정보를 DB에서 삭제해달라고 시킴
        attachmentRepository.deleteById(attachmentId);
    }

    // Quill 에디터용 이미지 업로드 메서드
    // 이미지는 게시글/댓글에 종속되지 않고 에디터 본문에 삽입되는 방식이라
    // post, comment 모두 null로 넘김
    public Attachment uploadImage(MultipartFile file) throws IOException {
        return saveFile(file, null, null);
    }

}
