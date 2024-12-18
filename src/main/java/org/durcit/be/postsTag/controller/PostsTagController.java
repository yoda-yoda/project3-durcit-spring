package org.durcit.be.postsTag.controller;

import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.post.service.PostService;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;
import org.durcit.be.postsTag.repository.PostsTagRepository;

import org.durcit.be.postsTag.service.PostsTagService;
import org.durcit.be.system.response.ResponseCode;
import org.durcit.be.system.response.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/{postId}") // URL을 {postId}로 한 이유는 몇번 게시물인지를 알아야하고, 그러려면 URL로 가져와야한다고 생각했기때문이다.
@RequiredArgsConstructor
public class PostsTagController {


    private final PostsTagService postsTagService;



    // 어떤 게시물에 붙어있는 tag 목록을 뿌려주기 위한 메서드다.
    // 이 메서드는 게시글 Id 를 URL에서 받아온다. 그리고 해당 게시글에 속하는 tag 엔티티들을 response Dto로 변환하고 모아서 List로 반환한다.
    // 만약 태그가 존재하지않으면 빈 dto 리스트를 반환하고, 존재하면 해당 태그엔티티를 변환한 dto list를 반환한다.
    @GetMapping("/posts-tag")
    public ResponseEntity<ResponseData<List<PostsTagResponse>>> getPostsTag(@PathVariable Long postId) {

        List<PostsTagResponse> postsTagResponseListByPostId = postsTagService.getPostsTagResponseListByPostId(postId);
        // 위 변수는 최종 응답 객체다.
        // 해당 게시물에 태그가 없다면 => 위 변수는, 비어있는 List 이다. 즉 null은 아니지만, is.empty() 가 true다.
        // 해당 게시물에 태그가 있다면 => 위 변수는, 태그 엔티티 객체를 응답 Dto로 변환하여 담은 List다.

        return ResponseData.toResponseEntity(ResponseCode.GET_POSTS_TAG_SUCCESS, postsTagResponseListByPostId);
    }

    @PostMapping("/posts-tag/members") // 유저가 인풋창에 태그 이름을 입력하고, 확인버튼을 눌러서 정상적으로 저장되는것을 가정했다.
    public ResponseEntity<ResponseData<List<PostsTagResponse>>> createPostsTag(List<PostsTagRegisterRequest> postsTagRegisterRequestList, @PathVariable Long postId) { // 유저가 입력한 태그 내용을 Dto List로 받고, postId도 경로에서 받아온다.
        List<PostsTagResponse> postsTagResponseList = postsTagService.createPostsTag(postsTagRegisterRequestList, postId);// 포스트태그서비스에서 메서드를 돌리고, 응답 dto List를 반환했다.
        return ResponseData.toResponseEntity(ResponseCode.CREATE_POSTS_TAG_SUCCESS, postsTagResponseList);
    }

    // post 할때 내려주자..
    // 만약 안 내려주는 판단을 했더라도 리액트에서 한번 더 요청 get으로 보내면 된다
    // 생성 했을때 내려주는게 좋겠다
    // 그럼 이경우에도 get처럼

    @PutMapping("/posts-tag/members/put")
    public ResponseEntity<ResponseData<List<PostsTagResponse>>> putPostsTag(List<PostsTagRegisterRequest> postsTagRegisterRequestList, @PathVariable Long postId) {
        List<PostsTagResponse> postsTagResponseList = postsTagService.updatePostsTag(postsTagRegisterRequestList, postId);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_POSTS_TAG_SUCCESS, postsTagResponseList);
    }


    @DeleteMapping("/posts-tag/members/delete")
    public ResponseEntity<ResponseData> deletePostsTag(List<Long> postsTagIdList) {
        postsTagService.deletePostsTagsByPostsTagId(postsTagIdList);
        return ResponseData.toResponseEntity(ResponseCode.DELETE_POSTS_TAG_SUCCESS);
    }





}
