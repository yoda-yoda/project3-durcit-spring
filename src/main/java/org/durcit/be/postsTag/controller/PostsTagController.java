package org.durcit.be.postsTag.controller;

import lombok.RequiredArgsConstructor;
import org.durcit.be.postsTag.domain.Posts;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.postsTag.service.PostsService;
import org.durcit.be.postsTag.service.PostsTagService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/{postId}") // URL을 {postId}로 한 이유는 몇번 게시물인지를 알아야하고, 그러려면 URL로 가져와야한다고 생각했기때문이다.
@RequiredArgsConstructor
public class PostsTagController {

    //
    // create update delete -> member
    // /api/members/posts-tag/{postId}/

    private final PostsTagService postsTagService;


    @GetMapping("/posts-tag") // tag 추가 버튼을(태그 팔로우 버튼을) 눌렀을때 이런 URL로 이동한다.
    public String getPostsTag(@PathVariable Long postId) {
        return "tag_input_modal_page"; // 이부분에서 모달창 페이지를 보여줘야하는데.
    }

    @PostMapping("/posts-tag") // 유저가 인풋창에 태그 이름을 입력하고, 확인버튼을 눌러서 정상적으로 저장되는것을 가정했다.
    public ResponseEntity<ResponseData<PostsTagResponse>> createPostsTag(PostsTagRegisterRequest postsTagRegisterRequest, @PathVariable Long postId) { // 유저가 입력한 태그 내용을 Dto로 받고, postId도 경로에서 받아온다.
        PostsTagResponse postsTagResponse = postsTagService.createPostsTag(postsTagRegisterRequest, postId); //포스트태그서비스에서 메서드를 돌리고, 응답 dto를 반환했다.
        return ResponseData.toResponseEntity(ResponseCode.CREATE_POSTS_TAG_SUCCESS, postsTagResponse); // 따라 만들었다.
    }

    // get 무조건 데이터가 필요하잖아요 구체적인 조회정보 //get방식일때 밑줄 return 위에꺼처럼하기.
    // return ResponseEntity<ResponseData<리턴할 디티오 타입>> 이걸 리턴하면 된다

    // post 할때 내려주자..
    // 만약 안 내려주는 판단을 했더라도 리액트에서 한번 더 요청 get으로 보내면 된다
    // 생성 했을때 내려주는게 좋겠다
    // 그럼 이경우에도 get처럼

    // update delete
    // 안내려줘도 되는 경우에는 응답코드만 넘겨주는 것
    // 즉 ResponseCode.CREATE_POSTS_TAG_SUCCESS 까지만.
    // ResponseEntity<ResponseData>>


    // 수정할때 일부만 수정하면 @PatchMapping
    // 전부 수정하면 @PutMapping
    // 삭제 할때 @DeleteMapping
    // axios





}
