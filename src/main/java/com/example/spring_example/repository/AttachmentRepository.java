package com.example.spring_example.repository;

import com.example.spring_example.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByPostId(Long postId);// postId로 해당 게시글의 첨부파일 전체를 찾아
    List<Attachment> findByCommentId(Long commentId);   // postId로 해당 댓글의 첨부파일 전체를 찾아

    // 반환 값이 없는(void) deleteByPostId 메서드. post_id에 해당하는 첨부파일 전체를 삭제함
    @Modifying  // 이 메서드가 DB 데이터를 변경하는 작업임을 Spring에게 알림
    // 이 어노테이션이 없으면 JPA가 삭제 작업에 트랜잭션을 적용하지 않아서 오류가 남
    void deleteByPostId(Long postId);   // 아무것도 돌려주지 않는(void) 메서드. postId에 해당하는 첨부파일 전체를 삭제
}
