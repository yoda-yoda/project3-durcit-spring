package org.durcit.be.postsTag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.postsTag.domain.Posts;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;
import org.durcit.be.postsTag.dto.PostsTagUpdateRequest;
import org.durcit.be.postsTag.repository.PostsRepository;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.system.exception.ExceptionMessage;
import org.durcit.be.system.exception.tag.OptionalEmptyPostsTagFindByIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.durcit.be.system.exception.ExceptionMessage.OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR;

// 되도록 익셉션은 전부 service 에서만하기.


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsTagService {


    private final PostsTagRepository postsTagRepository;
    private final PostsService postsService; // 이걸 주입받은이유 => @PathVariable 로 postId를 받아서 posts를 조회해온후 postsTag 필드변수에 할당하고싶기때문이다.



    @Transactional // 포스트태그 dto와 postId를 매개변수로 받아서 엔티티로 바꾼후 디비에 저장하는 메서드다.
    // 이때 매개변수에 postId 까지 있는이유 =>  몇번 게시글인지를 컨트롤러의 URL에서 따와야한다고 생각했기때문이다.
    // 왜냐면 포스트 태그를 추가하는 모달창에는 몇번 게시글인지 적는 폼태그 input 창이 아마도 없을거기 때문이다.
    // 따라서 몇번 게시글인지는 유저가 적어주는게아니라, 어딘가에서 가져와야하는데 그 어딘가를 컨트롤러의 URL이라고 생각했다.
    public PostsTagResponse createPostsTag(PostsTagRegisterRequest postsTagRegisterRequest, Long postId) {
        Posts one = postsService.findOne(postId);        // URL의 postId를 이용해 몇번게시글인지를 디비에서 조회해온다.
        postsTagRegisterRequest.setPosts(one);      // 디비에서 조회해온 해당 게시글을 태그Dto 객체인 postsTagRegisterRequest 필드에 set 해준다.
        PostsTag postsTag = PostsTagRegisterRequest.toEntity(postsTagRegisterRequest);      // 만들어둔 메서드로, Dto를 postsTag 엔티티로 변환한다.
        PostsTag saved = postsTagRepository.save(postsTag);     // 최종 완성된 postsTag 엔티티를 디비에 저장한다.
        return PostsTagResponse.fromEntity(saved);      // 최종 완성된 postsTag 엔티티를 응답데이터로 내려주기위해, PostsTagResponse 라는 Dto 객체로 변환하여 return 해준다.
    }


    public List<PostsTag> getAllPostsTags() {
        return postsTagRepository.findAll()
                .stream()
                .filter(tag -> !tag.isDeleted())
                .collect(Collectors.toList());      // 소프트딜리트를 제외한 모든 PostsTag 엔티티를 List로 모은다.
    }

    // 1번 게시물 태그 : 여행 독서 영화
    // 수정
    // 1번 게시물 태그 : 맛집

    // 시나리오
    // 기존에 있는 것들을 그냥 지우고 새로 만드는게 좋을 것 같다
    // setter 수정
    // 이런 여러개를 넣을 수 있는데 갯수도 정해지지 않는 것들 -> 태그, 이미지,
    // 세터 수정이 어려워요
    // 왜 어렵냐면
    // 세터 수정 예시를 들자면
    // 1 번 게시물을 수정하는 거니까 1번 게시물에 해당하는 태그를 다 불러와요
    // 수정하려고 하는 리스트랑 비교를 해야해요
    // 수정 리퀘스트 postId 1, contents 맛집
    // for 문을 써야함
    // 둘중 더 많을 기준으로 for문을 써야함
    // 왜냐면 기존에 있는게 더 많을 경우에는 기존에 있는 것들을 전부다 deleted 처리를 해줘야함
    // 만약 새로 생긴 태그 의 수가 더 많을 경우 얘네를 전부
    // 기존의 것을 바꾸는 건데
    // 태그가 몇개 들어올지 모름...

    // create 리스트로 받아야 한다

    // 수정 시나리오는 기존에 있던 것들은 지우고 새로 만들어서 넣어주면 된다
    // 여기에서도 논리 삭제
    // deleted 처릐를 다 해주고 새로 만들어서 넣어주면 된다.



    public PostsTag getPostsTagById(Long postsTagId) {  // 필요할까봐 만들어둔 메서드다. 일단 지금은 PostsTag 테이블의 pk 아이디로 자료를 찾아오는것이 유용할까 의심되는 중이다.
        return postsTagRepository.findById(postsTagId)
                .filter(tag -> !tag.isDeleted())
                .orElseThrow(() -> new OptionalEmptyPostsTagFindByIdException(OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR));      // 엔티티를 찾아오고 소프트딜리트를 필터링한다음 옵셔널아니면 반환하고, 옵셔널이면 오류를 생성한다.
    }

    public void getPostsTagByPostsId(Long postsId){  // 게시물의(posts 엔티티의) pk(id) 를 기준으로 postsTag 엔티티를 조회하는 메서드다.


    }



    public List<PostsTagResponse> getPostsTagByContents(PostsTagRegisterRequest postsTagRegisterRequest) {    // 메인에서 태그를 검색할때 PostsTag의 contents 를 기준으로 select 하여서 화면에 뿌려줘야하기때문에 필요하다. Dto로 받고 Dto로 반환할것이다.

        List<PostsTag> findByContentsCollect = postsTagRepository.findByContents(postsTagRegisterRequest.getContents())
                .stream()
                .filter(tag -> !tag.isDeleted())
                .collect(Collectors.toList());  // 검색한 태그 내용(contents) 중에서, 소프트 딜리트되지않는 엔티티들을 각각 필터하고 리스트에 담는다.

        List<PostsTagResponse> collect = findByContentsCollect
                .stream()
                .map(postsTag -> PostsTagResponse.fromEntity(postsTag))  // PostsTag 엔티티를 각각쪼개서 PostsTagResponse DTO로 변환한다.
                .collect(Collectors.toList());// 그것을 다시 리스트로 모은다.

        return collect; // 조회해온 검색 태그 자료를 Dto로 바꿔서 모은 최종 List를 return 한다.
    }
















//    public List<PostsTag> getAllPostsTag() {
//        return postTagRepository.findAll().stream().filter(tag -> !tag.isDeleted()).collect(Collectors.toList());
//    }

//    public PostsTag updatePostsTag(PostsTagUpdateRequest postsTagUpdateRequest, Long id) {
//
//        PostsTag postsTagById = getPostsTagById(id);
//
//
//
//        postsTagById.set
//    }




    // 삭제를 remove 를 안쓰고 setDeleted true 로 할것이다.


}
