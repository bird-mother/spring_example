package com.example.spring_example.repository;

import com.example.spring_example.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByPostId(Long postId);// postId로 해당 게시글의 첨부파일 전체를 찾아
    List<Attachment> findByCommentId(Long commentId);   // postId로 해당 댓글의 첨부파일 전체를 찾아

}
