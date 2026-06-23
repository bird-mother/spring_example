package com.example.spring_example.repository;

import com.example.spring_example.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    // postId(게시글 번호)로 해당 게시글의 댓글 전체를 찾아와
    // JPA가 메서드 이름을 분석하여 자동으로 쿼리를 만듬
    // SELECT * FROM comment WHERE post_id = ?

    @Modifying
        // 메서드가 DB 데이터를 변경하는 작업(INSERT, UPDATE, DELETE)임을 Spring에게 알림
    void deleteByPostId(Long postId);   // "postId에 해당하는 댓글 전체를 삭제해줘"
}
