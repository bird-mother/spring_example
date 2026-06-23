package com.example.spring_example.service;

import com.example.spring_example.entity.Post;
import com.example.spring_example.repository.AttachmentRepository;
import com.example.spring_example.repository.CommentRepository;
import com.example.spring_example.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;    // 트랜잭션 관리

import java.util.List;

@Service    // 이 클래스가 service 계층이라고 Spring에게 전달. Controller 에서 가져다 쓸 수 있게 해주는 표시
@RequiredArgsConstructor    // final 필드를 매개변수로 받는 생성자 자동 생성
//클래스 안에 선언한 final 필드를 자동으로 생성해주는 생성자를 lombok 이 만들어줌
//만약 집적 작성하게 된다고 하면 아래 코드와 같은데, 이것을 생략하게 해주는 어노테이션
//public PostService(PostRepository postRepository) {
//    this.postRepository = postRepository;
//}
public class PostService {

    private final PostRepository postRepository;    // Repository 를 주입함
    // final 로 생성하는 이유는, 한번 주입받고 수정되지 않도록 고정하기 위해서 이다.
    // Spring이 처음 객체 생성할 때 딱 한번 주입해주고 끝

    private final CommentRepository commentRepository;
    // 게시글 삭제 시 댓글을 먼저 삭제해야 하기 때문에 PostService에서 CommentRepository가 필요함.
    // final로 선언해서 한 번 주입받으면 바뀌지 않게 고정

    private final AttachmentRepository attachmentRepository;
    // 게시글 삭제 시 첨부파일을 먼저 삭제해야 하기 때문에 AttachmentRepository(첨부파일 DB 창구) 타입을 담는 attachmentRepository 변수를 선언

    // 게시글 전체 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();            // Repository 의 findAll() 호출
    }
//    Repository 의 메서드를 그대로 가져다 쓰는 부분이다
//    Service는 Repository를 호출만 하고 실제 DB 처리는 JPA가 처리한다

//    // 게시글 전체 조회(페이징)
//    public Page<Post> getPagedPosts(int page, int size){        // 반환해라, page번째 페이지의 게시글 size개를
//                Pageable pageable = PageRequest.of(page, size, Sort.by("id")
//                        .descending());     //  만들어라, 페이지 요청 설정을, page번째, size개씩, id 내림차순으로
//        return postRepository.findAll(pageable);        // 반환해, postRepository에서 pageable 설정대로 조회한 게시글 목록을
//    }

    // 게시글 목록 조회 (페이징 + 정렬 + 검색)
    public Page<Post> getPagedPosts(    // post를 page로 돌려주는 메서드
            int page, int size, String sort, String searchType, String keyword) {   // postPagePosts에 받을 매개변수

        // 정렬 옵션 - 삼항연산자
        Sort sortOption = sort.equals("viewCount")  // 정렬 옵션 선택 - sort가 viewCount인가?
                ? Sort.by("viewCount").descending()     // sort가 viewCount면 조회순
                : Sort.by("id").descending();   // sort가 id면 작성순

        // 페이징 기능
        // 페이지 번호(page), 페이지 크기(size), 정렬 방식(sortOption)을 설정해서 pageable 변수에 담음
        Pageable pageable = PageRequest.of(page, size, sortOption);

        // 검색 기능
        if (keyword != null && !keyword.isEmpty()) {    // keyword(검색어)가 null이 아니고 빈 문자열도 아닌 경우에만 아래 코드를 실행
            if (searchType.equals("userName")) {    // searchType이 "userName"이면
                //postRepository한테 작성자명에 keyword가 포함된 게시글을 페이징해서 찾아달라고 시키고 결과를 돌려줘
                return postRepository.findByUserNameContaining(keyword, pageable);
            }
            // searchType이 "userName"이 아니면 제목에 keyword가 포함된 게시글을 페이징해서 찾아달라고 시키고 결과를 돌려줌
            return postRepository.findByTitleContaining(keyword, pageable);
        }
        // 검색어가 없으면 postRepository한테 전체 게시글을 페이징해서 가져달라고 시키고 결과를 돌려줌
        return postRepository.findAll(pageable);
    }

    // 게시글 작성
    public Post createPost(Post post){
        return postRepository.save(post);           // Repository 의 save() 호출
    }

//    // 게시글 단건(1개) 조회
//    public Post getPost(Long id){       // Long 타입의 PK 로 설정한 id 를 post 에서 가져와
//        return postRepository.findById(id)      // postRepository 에 있는 id 를 반환해
//                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다. id : "+id));   // () -> 만약 값이 없으면 화살표 뒤에 예외를 던져
//    }

    //게시글 단건 조회 및 조회수 증가
    public Post getPost(Long id){       // Long 타입의 PK 로 설정한 id 를 post 에서 가져와
        Post post = postRepository.findById(id)     //  찾아라, postRepository에서, id에 해당하는 게시글을
                .orElseThrow(()->new RuntimeException("게시글이 존재하지 않습니다. id : "+id));     // 없으면 에러를 던져
        post.setViewCount(post.getViewCount()+1);       // 바꿔라, post의 조회수를, 현재 조회수에 1을 더한 값으로
        return postRepository.save(post);       // 반환해라, postRepository에 저장한 post를.
    }

    // 게시글 수정
    public Post updatePost(Long id, Post updatedPost){  // Post를 돌려주는 updatePost 라는 메서드인데, id랑 updatedPost(새로운 게시글)를 입력값으로 받는다
        Post post = postRepository.findById(id)     // postRepository(DB 창구)한테 .findById(id)(이 번호로 게시글 찾아줘)라고 시킨다. 찾은 결과를 post(기존 게시글을 담을 그릇)에 담는다
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다. id : "+id));   // 만약 못 찾으면 .orElseThrow(...)(에러를 던져라)가 실행된다

        post.setTitle(updatedPost.getTitle());  // updatedPost(새 데이터)에서 제목을 꺼내서, post(기존 게시글)의 제목 자리에 덮어쓴다
        post.setContent(updatedPost.getContent());  // updatedPost(새 데이터)에서 내용을 꺼내서, post(기존 게시글)의 내용 자리에 덮어쓴다
        post.setUserName(updatedPost.getUserName());    // updatedPost(새 데이터)에서 작성자를 꺼내서, post(기존 게시글)의 작성자 자리에 덮어쓴다

        // 내용이 바뀐 post를 postRepository한테 건네서 .save(저장해줘)라고 시킨다. 이 post는 이미 id가 있는 상태라서, DB에는 새로 추가(INSERT)가 아니라 기존 걸 덮어쓰는(UPDATE)로 처리된다. 저장된 결과를 그대로 돌려준다
        return postRepository.save(post);
    }

    // 게시글 삭제
    @Transactional
    // 이 메서드 안의 모든 DB 작업을 하나의 트랜잭션으로 묶음
    public void deletePost(Long id) {
        // 반환 값이 없는(void) deletePost 메서드. 삭제할 게시글 번호 id를 입력값으로 받음

        attachmentRepository.deleteByPostId(id);
        // attachmentRepository한테 id에 해당하는 게시글의 첨부파일 전체를 삭제해달라고 시킴

        commentRepository.deleteByPostId(id);
        // commentRepository한테 id에 해당하는 게시글의 댓글 전체를 삭제해달라고 시킴

        postRepository.deleteById(id);
        // postRepository한테 id에 해당하는 게시글을 삭제해달라고 시킴
    }

    // 게시글 검색(제목 키워드)
    public List<Post> searchPost(String keyword){       // Post 목록을 담은 List를 돌려주는 메서드를 keyword를 입력값으로 받아
        return postRepository.findByTitleContaining(keyword);       // 반환해. postRepository에서 keyword가 포함된 게시글 목록을

    }
}
