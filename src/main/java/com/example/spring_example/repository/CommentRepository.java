package com.example.spring_example.repository;

import com.example.spring_example.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    // postId(게시글 번호)로 해당 게시글의 댓글 전체를 찾아와
    // JPA가 메서드 이름을 분석하여 자동으로 쿼리를 만듬
    // SELECT * FROM comment WHERE post_id = ?
}
