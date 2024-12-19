package org.durcit.be.postsTag.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.domain.Post;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.postsTag.service.PostsTagService;
import org.durcit.be.system.exception.post.PostNotFoundException;
import org.durcit.be.system.exception.tag.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.durcit.be.system.exception.ExceptionMessage.*;

// 되도록 익셉션은 전부 service 에서만하기.
// 왠만하면 모든 요청은 Dto로 받고 응답도 Dto로 반환할것이다.

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsTagServiceImpl implements PostsTagService {


    private final PostsTagRepository postsTagRepository;



    @Transactional
    // 이 메서드는 포스트태그 dto와 게시물Id를 매개변수로 받아서 엔티티로 바꾼후 디비에 저장하는 메서드다.
    // 241219 기준으로, 기존에 존재하는 태그를 추가할시에 막는 유효성 검사 로직을 추가해야한다. 아직 설계중입니다.
    public List<PostsTagResponse> createPostsTag(List<PostsTagRegisterRequest> postsTagRegisterRequestList, Long postId) {

        Post postGetById = postsTagRepository.findPostByPostId(postId);        // 컨트롤러에서 넘어온 postId를(URL에 따온 postId를) 이용해 몇번게시글인지를 디비에서 조회해온다.


        List<PostsTagResponse> postsTagResponseList = new ArrayList<>(); // 응답 Dto 들을 모을 List를 만들었다.


            for (PostsTagRegisterRequest postsTagRegisterRequest : postsTagRegisterRequestList) { // foreach 문을 통해서, 리스트 안에 실존하는 하나하나의 각 객체들에다가 { } 안의 명령들을 먹일것이다.

                PostsTag postsTag = PostsTagRegisterRequest.toEntity(postsTagRegisterRequest, postGetById); // postsTagRegisterRequestList 안의 각각의 객체들을, toEntity 메서드를 통해 PostsTag entity로 생성(변환)해준다. 이때 post 필드도 할당하기위해 2번째 인자로 넣어줬다.


                PostsTag savedPostsTag = postsTagRepository.save(postsTag); // 디비에 postsTag 엔티티를 저장해준다.
                PostsTagResponse postsTagResponse = PostsTagResponse.fromEntity(savedPostsTag); // 각각의 postsTag 엔티티를 PostsTagResponse 타입의 Dto로 변환해준다. 응답에 보내주기 위한 Dto다.
                postsTagResponseList.add(postsTagResponse); // 응답 Dto 들을 List로 한군데 모아준다. 응답에 List로 보내려하기 때문이다.
            }


        return postsTagResponseList; // 최종 응답 Dto List 를 호출한곳으로 반환. 만약 유저가 추가한 모든 태그가 모두 이미 존재하는 태그라면 최종 응답은 빈 리스트로 반환 될것이다.

    }




    // 테그 테이블 중에 소프트딜리트된것 뺀 모든 엔티티를 리스트로 반환하는 메서드.
    //  DB에 저장이 되었지만 전부가 딜리트처리 되어있다면 정의한 오류를 던진다. 또한 DB 저장된 것이 아예 없는 상태여도 오류를 던진다.
    public List<PostsTag> getAllPostsTagsWithNonDeleted() {
        List<PostsTag> collect = postsTagRepository.findAll()
                .stream()
                .filter(tag -> !tag.isDeleted())
                .collect(Collectors.toList());// 소프트딜리트 된것들을 제외한 모든 PostsTag 엔티티를 List로 모아서 반환한다. 그런게 없다면 빈 리스트다.

        if (collect.isEmpty()) {    // 빈 리스트라면 정의한 오류를 던진다.
            throw new NoPostsTagInListTypeException(NO_POSTS_TAG_IN_LIST_TYPE_ERROR);
        }

        return collect;
    }



    // DB 존재하는 테그 테이블 중에, 소프트딜리트 상관없이 그냥 모든 엔티티를 리스트로 반환하는 메서드.
    // 아예 비어있다면 오류를 던진다.
    public List<PostsTag> getAllPostsTags() {

        List<PostsTag> all = postsTagRepository.findAll();

        if ( all.isEmpty() ) {  // 비어있다면 오류던지기. 즉 디비에 저장된게 아예없을 경우일것이다.
            throw new NoPostsTagInListTypeException(NO_POSTS_TAG_IN_LIST_TYPE_ERROR);
        }

        return all;  // 디비에 저장되어있기만하다면 모든 PostsTag 엔티티를 List로 모아서 반환한다.

    }


    // 태그테이블 중에 pk를 기준으로 찾고 소프트딜리트 true면 오류던지고, 아니면 해당 엔티티 1개 반환하는 메서드
    public PostsTag getPostsTagById(Long postsTagId) {
        return postsTagRepository.findById(postsTagId)
                .filter(tag -> !tag.isDeleted())
                .orElseThrow(() -> new OptionalEmptyPostsTagFindByIdException(OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR));

    }


    // 소프트딜리트 되어있든 안되어있든 postId 를 기준으로 Post가 가지고있는 태그들을 list에 담아서 반환하는 메서드.
    // 만약 해당 포스트에 저장된 태그가 아예없다면 오류를 던진다.
    public List<PostsTag> getPostsTagListByPostId(Long postId) {

        Post postById = Optional.ofNullable(postsTagRepository.findPostByPostId(postId))
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));        // postId 로 찾았는데 post가 없다면 오류 던지고, 있으면 할당했다.


        List<PostsTag> getList = postById.getPostsTagList();
        // 해당 게시글이 어떤 태그리스트를 갖고있는지 getter 로 얻어서 담는다. getter로 얻기때문에 소프트딜리트가 되어있어도 상관없이 담길것이다.
        // 만약 게시글에 태그가 없다면 빈 리스트일것이다.

        if ( getList.isEmpty() ) {  // 빈리스트면(해당 게시글에 아무 태그리스트가 없다면) 정의한 오류를 던진다.
            throw new EmptyPostsTagListInPostException(EMPTY_POSTS_TAG_LIST_IN_POST_ERROR);
        }

        return getList;
        // 해당 게시글과 연동된 태그 DB에 어떤 태그든(소프트 딜리트 되었어도) 저장되어있기만 했다면,
        // 모든 태그 객체가 리스트에 담겨 이렇게 반환된다.

    }




    // 이 메서드는 게시물Id 를 기준으로 Post 엔티티를 조회한다. 그리고 해당 엔티티 내부의 연관관계인 PostsTagList를 소프트딜리트 필터링해서 반환하는 메서드다.
    public List<PostsTag> getNoneDeletedPostsTagListByPostId(Long postId) {


        Post postById = Optional.ofNullable(postsTagRepository.findPostByPostId(postId))
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));        // postId 로 찾았는데 post가 없다면 오류 던지고, 있으면 할당했다.


        List<PostsTag> getList = postById.getPostsTagList();
        // 해당 게시글이 어떤 태그리스트를 갖고있는지 getter 로 꺼내본다. 없다면 디비에 아예 없는것이며 빈 리스트가 나온다.

        List<PostsTag> noneDeletedList = new ArrayList<>();
        // 나중에 최종 반환값을 여기에 담기위해 미리 정의 해줬다.


        // 게시글이 DB상에서 (소프트 딜리트 처리가 됐든 안됐든) 어떤 태그를 갖고있어야지만 if문 안에 진입할수있다. 진입하게되면 stream()을 이용하여 소프트 딜리트를 필터링을하고 변수에 할당한다.
        // 그런데, 만약 모든 태그가 delete true 처리됐다면, noneDeletedList 는 빈 리스트 인채로 이 if문을 빠져나갈것이다.
        if( !getList.isEmpty() ){

            noneDeletedList =
                    getList.stream()
                    .filter(postsTag -> !postsTag.isDeleted())
                    .collect(Collectors.toList());
        }


        if ( getList.isEmpty() ) {
            throw new NoPostsTagInListTypeException(NO_POSTS_TAG_IN_LIST_TYPE_ERROR);
            // 해당 게시글에 대해 DB에 저장된 태그가 없다면 오류를 던진다.

        } else if ( noneDeletedList.isEmpty() ) {
            throw new NoPostsTagInListTypeException(NO_POSTS_TAG_IN_LIST_TYPE_ERROR);
            // 해당게시글에이 전부 소프트 딜리트된 태그만 갖고있다면 오류를 던진다.
        }

        else {
            return noneDeletedList;
            // 최종적으로 딜리트되지않은 태그 객체만 담긴 리스트만 반환된다.

        }

    }


    // 위의 메서드와 비슷하다. postId 로 찾은 해당 Post 엔티티 내부의 연관관계인 PostsTagList 중에서 delete 처리 안된것들만 뽑되, Response로 변환하여 반환해주는 메서드다.
    // 만약 태그가 디비에 아예 저장이 안된경우,
    // 그리고 디비엔 있지만 모두 소프트딜리트 처리된 경우라면
    // 비어있는 List 를 반환한다.
    public List<PostsTagResponse> getPostsTagResponseListByPostId(Long postId) {

        Post post = Optional.ofNullable(postsTagRepository.findPostByPostId(postId))
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR)); // 해당 아이디의 게시글을 찾고 없으면 오류를 낸다.


        List<PostsTag> postsTagListByPostId = post.getPostsTagList(); // 해당 게시글의 태그 디비를 조회한다.


        if ( postsTagListByPostId.isEmpty() ) {
            return new ArrayList<>();
            // 해당 게시물에 대한 태그 디비에 delete true든 아니든 아예 저장되어있는게 없다면, 빈 List를 반환하고 메서드를 종료한다.
            // 즉 결국 최종 반환 타입인 List<PostsTagResponse> 는 빈 List가 된다. (null은 아니지만 .isEmpty()가 true인 상태다.)
        }



        // 여기오면 태그 디비에 무엇이든 저장은 되어있다는 것이고, 여기서 소프트딜리트 필터링을 해준다.
        // 딜리트가 안된 태그가 존재한다면, 이 filterCollect 리스트에 담긴다.
        List<PostsTag> filterCollect = postsTagListByPostId.stream()
                .filter(postsTag -> !postsTag.isDeleted())
                .collect(Collectors.toList());


        if ( filterCollect.isEmpty() ) { // 여기 진입하면 모든 태그가 딜리트처리 되어있다는 뜻이므로 마찬가지로 빈 리스트를 반환하고 메서드를 종료한다.
            return new ArrayList<>();
        }


        List<PostsTagResponse> postsTagResponseList = new ArrayList<>();        // 응답Dto를 담을 List를 만들어준다.

        for (PostsTag postsTag : filterCollect) {
            postsTagResponseList.add(PostsTagResponse.fromEntity(postsTag));    // 각 PostsTag 엔티티들을 fromEntity 메서드를 이용해 응답 Dto로 변환해주고 만들어둔 List에 추가해준다.
        }

        return postsTagResponseList;        // 최종 응답 객체 리스트를 반환한다.
    }




    // 메인에서 어떠한 태그를 1개 검색하면, 태그 테이블에서 검색과 일치하는 contents 를 기준으로 찾아 select 해온후 해당 게시글을 화면에 뿌려줘야하기때문에,
    // 그 과정에 필요할것같아서 만든 메서드이다.
    // 즉 유저가 검색한 태그내용과 일치하는 엔티티들 중에, 소프트딜리트를 제외하고 List로 반환하는 메서드다.
    // 만약 DB상으로 해당 검색 태그가 아예없거나, 있어도 전부 다 소프트 딜리트 처리된상태라면, 오류를 던진다.
    public List<PostsTag> getPostsTagByContents(PostsTagRegisterRequest postsTagRegisterRequest) {

        List<PostsTag> postsTagList = postsTagRepository.findByContents(postsTagRegisterRequest.getContents() );
        // Dto 필드에 할당된 태그 내용을 토대로 엔티티들을 디비에서 전부 조회한다.


        if ( postsTagList.isEmpty() ) {          // 여기 진입하면 해당 태그를 검색해도 디비에 아예 없다는것이다. 그리고 오류를 던진다.
            throw new NoPostsTagInListTypeException(NO_POSTS_TAG_IN_LIST_TYPE_ERROR);
        }



        List<PostsTag> filterCollect = postsTagList.stream()        // 이제 딜리트 처리를 필터링한다.
                .filter(postsTag -> !postsTag.isDeleted())
                .collect(Collectors.toList());



        if ( filterCollect.isEmpty() ) {        // 여기 진입하면 디비에 검색한 태그들이 존재하긴하지만 전부 딜리트 처리됐다는것이다. 그리고 오류를 던진다.
            throw new NoPostsTagInListTypeException(NO_POSTS_TAG_IN_LIST_TYPE_ERROR);
        }

        return filterCollect;       // 최종 검색된 태그들이 list에 담겨 반환된다.
    }



    
    // update 로직  => postId를 가진 태그를 전부 소프트딜리트한다.
    // 새로운 태그 리스트 요청을 받아서 다시 등록 처리한다.
    @Transactional
    public List<PostsTagResponse> updatePostsTag(List<PostsTagRegisterRequest> postsTagRegisterRequestList, Long postId) {  // 레지스터 리퀘스트 Dto로 사용했다.


        Post post = Optional.ofNullable(postsTagRepository.findPostByPostId(postId))
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));    // 해당 post를 찾는다. 없으면 오류를 던진다.


        List<PostsTag> postsTagList = post.getPostsTagList();
        // 해당 id의 게시글이 가진 tag들 전부를(소프트 딜리트된것 까지도) 찾아서 List에 담았다.
        // 즉 딜리트 처리 됐더라도 DB에 존재하는 태그라면 할당이 된다.
        // 만약 DB에 저장된 태그가 아예 없다면 빈 List 가 할당된다.



        // 빈 List가 아닐때만(태그 디비에 뭐든 저장이 되어있을때만) 진입하여서 딜리트 처리를 한다.
        // 빈 List가 여기 들어가면 널포인트익셉션이 뜰것같아서 이렇게했다.
        // 이미 딜리트처리된것들도 일단 그냥 다시 딜리트처리된다.
        if( !postsTagList.isEmpty() ) {

            for (PostsTag postsTag : postsTagList) {
                postsTag.setDeleted(true);
                postsTagRepository.save(postsTag);
            }

        }


        List<PostsTagResponse> postsTagResponseList = createPostsTag(postsTagRegisterRequestList, postId);
        // 내부 create 메서드를 이용해 요청객체 리스트를 응답객체 리스트로 반환했다.


        return postsTagResponseList;   // 수정 요청 객체에 담긴 값들을 db에 저장한 후 최종 응답 DTO를 반환한다.

    }



    @Transactional // 태그id를 1개만 받아 해당 태그 1개만 소프트 딜리트 하는 메서드
    public void deleteOnePostsTagByPostsTagId(Long postsTagId) {
        PostsTag postsTag = postsTagRepository.findById(postsTagId)
                .orElseThrow( () -> new OptionalEmptyPostsTagFindByIdException(OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR));  // 혹시 옵셔널이 null이면 오류던지기.
        postsTag.setDeleted(true);  // 논리 삭제하기.
        postsTagRepository.save(postsTag);  // 변경감지로 인해 논리삭제로 변경후 디비에 저장될것이다.

    }


    @Transactional // 태그id를 여러개 받아 해당하는 태그들을 전부 소프트 딜리트 하는 메서드
    public void deletePostsTagsByPostsTagId(List<Long> postsTagIdList) {
        for (Long l : postsTagIdList) {
            PostsTag postsTag = postsTagRepository.findById(l).orElseThrow(() -> new OptionalEmptyPostsTagFindByIdException(OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR));
            postsTag.setDeleted(true);
            postsTagRepository.save(postsTag);
        }   // 태그 id로 디비에서 조회한다. 혹시 옵셔널이면 오류던지고, 아니면 각 엔티티를 소프트딜리트 처리한다. 그리고 다시 변경감지를 활용해 디비에 저장한다.

    }


    // 241218 추가 => post id를 받아 해당 게시글의 모든 Tag를 소프트딜리트하는 메서드
    // 해당 Post가 갖고있는 모든 Tag List를 조회하고 디비에 아예 비어있으면(소프트 딜리트 되어있는것 조차도 아예없으면) 오류를 던지게했다.
    @Transactional
    public void deletePostsTagsByPostId(Long postId) {


        // 매개변수로 게시글 pk를 받고, 해당하는 post를 조회하고 없으면 오류던지고 있으면 반환했다.
        Post post = Optional.ofNullable(postsTagRepository.findPostByPostId(postId))
                        .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_ERROR));



        //해당 Post가 갖고있는 모든 Tag를(딜리트되었든 안되었든) 조회해서 담는다.
        List<PostsTag> postsTagList = post.getPostsTagList();


        //  만약 디비에 저장된 태그가 아예 없으면 여기 진입하고 오류를 던진다.
        if ( postsTagList.isEmpty() ) {
            throw new EmptyPostsTagListInPostException(EMPTY_POSTS_TAG_LIST_IN_POST_ERROR);
        }


        // 반복자를 통해서, 각 태그들을 전부 세터로 소프트딜리트처리하고 다시 디비에 저장한다.
        // 만약 이미 딜리트됐더라도 일단 그냥 다시 처리한다.
        for (PostsTag postsTag : postsTagList) {
            postsTag.setDeleted(true);
            postsTagRepository.save(postsTag);
        }

    }













}
