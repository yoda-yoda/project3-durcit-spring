package org.durcit.be.postsTag.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.durcit.be.postsTag.domain.Posts;
import org.durcit.be.postsTag.domain.PostsTag;
import org.springframework.validation.annotation.Validated;

// Dto에 @Valid 하기로함.

@Getter
@Setter
public class PostsTagRegisterRequest {

    @NotNull
    private String contents;

    private Long postId;
    private Posts posts;  // 태그를 추가할 게시물인지 어떤 게시물인지 알아야한다는 생각으로 넣었다.
    // NotNull을 뺀이유 => 모달창에서 input으로 태그내용을 넣을떄, Post를(게시물을) 넣는 인풋창은 없을거고,
    // 그 상태라면 낫널이 있으면 무조건 에러가 나게되는 상황이기 때문에 뺐다.
    // 그러면 이 posts 필드는 언제 초기화되느냐 => 컨트롤러 URL 경로변수에서 postId를 따온후 DB의 post 테이블에서 가져온다음 그것을 이 필드에 넣을 계획이다.

    public static PostsTag toEntity(PostsTagRegisterRequest request){
        return PostsTag.builder()
                .contents(request.getContents())
                .posts(request.getPosts())
                .build();
    }


}
