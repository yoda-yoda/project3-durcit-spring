//package org.durcit.be.system.aop;
//
//import org.durcit.be.post.domain.Post;
//import org.durcit.be.post.service.PostService;
//import org.durcit.be.security.domian.Member;
//import org.durcit.be.security.service.MemberService;
//import org.durcit.be.security.util.MockSecurityUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class PostAuthorizationAspectTest {
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private PostService postService;
//
//    @Autowired
//    private TestService testService;
//
//    @Test
//    void testServiceShouldBeProxy() {
//        System.out.println("TestService Class: " + testService.getClass());
//    }
//
//    @Test
//    @DisplayName("수정 및 삭제 권한이 있는 계정으로 접속시 메서드 바로 실행 -> 성공")
//    void should_allow_access_when_user_is_author_or_has_role() throws Exception {
//        // given
//        Long currentUserId = 1L;
//        Long postId = 2L;
//        MockSecurityUtil.mockSecurityContext(currentUserId);
//
//        Member member = Member.builder().id(currentUserId).build();
//
//        Post post = Post.builder()
//                .id(postId)
//                .member(member)
//                .build();
//
//        when(memberService.hasRole(currentUserId, new String[]{"ADMIN"})).thenReturn(false);
//        when(postService.getById(postId)).thenReturn(post);
//
//        // when
//        testService.updatePost(postId);
//
//        // then
//        verify(memberService, times(1)).hasRole(currentUserId, new String[]{"ADMIN"});
//        verify(postService, times(1)).getById(postId);
//    }
//
//}