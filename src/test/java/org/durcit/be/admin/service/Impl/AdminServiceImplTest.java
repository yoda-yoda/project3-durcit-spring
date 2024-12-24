package org.durcit.be.admin.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.durcit.be.admin.service.AdminService;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Slf4j
@Transactional
@Rollback(false)
class AdminServiceImplTest {


    @Autowired
    private AdminService adminService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostsTagRepository postsTagRepository;





    @Test
    @DisplayName("다음 test를 위해 DB를 미리 셋팅해놓는 setting 메서드1 - Post 안의 태그리스트가 트랜젝션이 1번돌지않으면 null인 이슈와 관련한것. ")
    void setting_method1() throws Exception {

        // given
        Post post1 = Post.builder()
                .content("내용1")
                .views(1L)
                .title("제목1")
                .build();

        Post post2 = Post.builder()
                .content("내용2")
                .views(1L)
                .title("제목2")
                .build();

        Post post3 = Post.builder()
                .content("내용3")
                .views(1L)
                .title("제목3")
                .build();


        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);


        PostsTag tag1 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post1)
                .build();


        PostsTag tag2 = PostsTag.builder()
                .contents("메이플스토리")
                .post(post2)
                .build();


        PostsTag tag3 = PostsTag.builder()
                .contents("바람의나라")
                .post(post2)
                .build();


        postsTagRepository.save(tag1);
        postsTagRepository.save(tag2);
        postsTagRepository.save(tag3);


        // when



        // then

       


    }




    @Test
    @DisplayName("트랜젝션 이슈때문에(트랜젝션이 1번은 끝나야 Post안의 TagList가 초기화되는 이슈때문에) 테스트를 한메서드에서 못하니까, 따로 만들어놓은 hardDeletePostAndPostsTag 메서드 테스트")
    void hardDeletePostAndPostsTag_method_test() throws Exception {
    		// given

    	  // when
        // 결과1  // adminService.hardDeletePostAndPostsTag(2L); // 포스트1,3 남아있어야함. 태그 1개 남아있어야함. => 통과.
        // 결과2 // adminService.hardDeletePostAndPostsTag(3L); // 위에꺼 한 상태에서 포스트만 1개 지워져야함. => 통과.
        // 결과3 //   adminService.hardDeletePostAndPostsTag(1L); // 위에꺼 한 상태에서 포스트만 1개 지워져야함. 그래서 db에 남은건 아무것도없음 => 통과.


    	  // then

        // 결과1:(해당 포스트 삭제하면, 해당포스트와 존재하는 해당 포스트의 태그까지 전부 물리삭제 ok) ok
        // 테스트 주석한 이유 :
        // 트랜젝션이 1번은 끝나야, Post안의 TagList가 null이 아니게 된다. 그래서 setting 메서드를 1번 실행해놓고,
        // ddl auto를 none으로 바꾸고 따로 작업을 해주기 위한 test다.


    }



}