package com.example.spring_example.service;

import com.example.spring_example.entity.Comment;
import com.example.spring_example.entity.Post;
import com.example.spring_example.repository.CommentRepository;
import com.example.spring_example.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;  // DB에 댓글을 저장하는 상수 필드 선언 - 댓글 저장, 조회, 수저으 삭제
    private final PostRepository postRepository;    // 게시글을 저장하는 상수 필드 선언 - 댓글 작성시, 게시글이 존재하는지 확인

    // 댓글 작성
    public Comment createComment(Long postId, Comment comment){     // 댓글을 달 게시글의 번호와 댓글 내용이 담긴 comment를 입력값으로 받음
        Post post = postRepository.findById(postId)     // postRepository에서 게시글 ID를 찾아달라고 시키고 있으면 post 변수에 담아
                .orElseThrow(() -> new RuntimeException( "게시글이 존재 하지 않습니다. id : "+postId));     // 없으면 에러를 던짐
        comment.setPost(post);      // 새로 작성할 댓글과 psot(게시글)을 연결한다.
        return commentRepository.save(comment);     // post 가 연결된 comment를 commentRepository에 전달하여 DB에 저장
    }

    // 댓글 조회(게시글에 있는 댓글 전체)
    public List<Comment> getComments(Long postId){      // Comment를 여러개 담은 List를 돌려주는데, 게시글 번호(postId)를 입력값으로 받음
        return commentRepository.findByPostId(postId);      // commentRepository 한테 posId에 달린 댓글 전체를 찾아달라 시킴
    }

    // 댓글 수정
    public Comment updateComment(Long commentId, Comment updateComment){        // 수정할 댓글 번호(commentId)와 수정할 내용이 담긴, updateComment를 입력값으로 받음
        Comment comment = commentRepository.findById(commentId)     // commentRepository한테 commentId로 댓글을 찾으라고 시키고 있으면 comment에 담고
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다. id : "+commentId));     // 없으면 에러를 던진다.
        comment.setContent(updateComment.getContent());     // comment를 수정(setCommet)할거야.updateComment에 수정한 내용을 comment에 덮어써야 하니까 Comment를 가져와(getComment)
        return commentRepository.save(comment);     // commentRepository에 저장해. 내용이 바뀐 comment를
    }

    // 댓글 삭제
    public void deleteComment(Long commentId){      // 아무것도 돌려주지 않는 delete메서드에 삭제할 댓글 번호를 입력 값으로 받음
        commentRepository.deleteById(commentId);        // commentRepository 한테 commentId 로 댓글을 지우라고 시킴
    }
}
