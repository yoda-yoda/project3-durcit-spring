package org.durcit.be.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.search.dto.TagSearchRequest;
import org.durcit.be.search.repository.TagSearchRepository;
import org.durcit.be.search.service.TagSearchService;
import org.durcit.be.system.exception.post.PostNotFoundException;
import org.durcit.be.system.exception.tag.NoPostsTagInListTypeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.durcit.be.system.exception.ExceptionMessage.NO_POSTS_TAG_IN_LIST_TYPE_ERROR;
import static org.durcit.be.system.exception.ExceptionMessage.POST_NOT_FOUND_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagSearchServiceImpl implements TagSearchService {



    private final TagSearchRepository tagSearchRepository;





    // 메서드 기능: 유저가 검색한 태그를 검색한다. 그리고 태그와 Post가 존재하고 둘다 delete false 인 경우만 찾아서, 응답 List로 변환 후 반환한다.
    // 반환: 해당하는 List<PostCardResponse> 를 반환하고, 없으면 빈 리스트를 반환한다.
    // 빈 List 반환 경우1: 검색한 Tag가 DB에 아예 없으면 빈 List를 반환한다.
    // 빈 List 반환 경우2: 찾은 Tag들이 테이블에서 전부 delete true 상태면 빈 List를 반환한다.
    // 빈 List 반환 경우3: Tag가 있고 delete false 이긴하지만, 해당 Post 자체가 delete true면 빈 List를 반환한다.
    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeletedPostsTag(TagSearchRequest tagSearchRequest) {



        // 검색한 tag의 String을 요청 Dto에서 뽑아낸다.
        String contents = tagSearchRequest.getContents();


        // tag테이블에서 해당 tag이면서 delete false인 엔티티들을 찾는다. 해당하지않으면 빈리스트(isEmpty() == true) 로 담긴다.(null은 아니다.)
        List<PostsTag> postsTags = tagSearchRepository.findPostsTagByContentsWithNoneDeleted(contents);


        // 해당하는 것을 PostsTag 테이블에서 찾을수없다면 빈 리스트를 반환하고 메서드를 종료한다.
        if( postsTags.isEmpty() ) {
            return new ArrayList<>();
        }


        // 해당 Post 들을 담아두려고 List를 만들었다.
        // 비어있는지 검사하거나, 반복문 안에서 응답Dto로 변환하기 위함이다.
        List<Post> checkedAllPosts = new ArrayList<>();


        // 태그도 존재하고, 태그의 delete도 false인 Post 들을 반복자로 검사한다.
        // delete가 false인 Post들만 담는다. 전부 true면 빈 리스트이다.
        for (PostsTag postsTag : postsTags) {

            if( !postsTag.getPost().isDeleted() ){
                checkedAllPosts.add(postsTag.getPost());
            }
        }
        

        // 해당하는 Post가 전부 delete true라면 빈 리스트를 반환한다.
        if( checkedAllPosts.isEmpty() ){
            return new ArrayList<>();
        }


        // 최종 반환할 응답 List를 미리 만들었다.
        List<PostCardResponse> postCardResponses = new ArrayList<>();


        // 반복자를 활용해서, 모든 해당 Post들을 각각 응답 Dto로 변환하고 List에 add한다.
        for (Post post : checkedAllPosts) {
            postCardResponses.add(PostCardResponse.fromEntity(post));
        }


        // 조건에 맞는 최종 응답List를 반환한다.
        return postCardResponses;


    }







    // 메서드 기능: 유저가 검색한 태그가 존재하는지 먼저 살핀다. 그리고 태그테이블과 Post테이블 둘다 delete false 인 경우를 찾아서, 해당 게시글을 List로 반환하는 메서드다.
    // 예외 경우1: 검색한 Tag가 DB에 아예 없으면 예외를 던진다.
    // 예외 경우2: 찾은 Tag들이 테이블에서 전부 delete true 상태면 예외를 던진다.
    // 예외 경우3: Tag가 있고, delete false 여도 해당 Post 자체가 delete true면 예외를 던진다.
    // 반환: 해당하는 List<Post> 를 반환하고, 없으면 예외다.
    public List<Post> getAllPostsWithNoneDeletedPostsTag(TagSearchRequest tagSearchRequest) {


        // 검색한 tag의 String을 요청 Dto에서 뽑아낸다.
        String contents = tagSearchRequest.getContents();


        // tag테이블에서 해당 tag이면서 delete false인 엔티티들을 찾는다. 해당하지않으면 빈리스트(isEmpty() == true) 로 담긴다.(null은 아니다.)
        List<PostsTag> postsTags = tagSearchRepository.findPostsTagByContentsWithNoneDeleted(contents);


        // 해당하는 것을 PostsTag 테이블에서 찾을수없다면 오류를 던지고 메서드를 종료한다.
        if( postsTags.isEmpty() ) {
            throw new NoPostsTagInListTypeException(NO_POSTS_TAG_IN_LIST_TYPE_ERROR);
        }


        // 최종 해당 Post 들을 담을 List를 미리 만들었다.
        // 만약 Post가 전부 delete true 면 밑에서 오류를 던질것이다.
        List<Post> checkedAllPosts = new ArrayList<>();


        // 태그도 존재하고, 태그의 delete도 false인 Post 들을 반복자로 검사한다.
        // delete가 false인 Post들만 최종 반환에 add한다.
        for (PostsTag postsTag : postsTags) {

            if( !postsTag.getPost().isDeleted() ){
                checkedAllPosts.add(postsTag.getPost());
            }
        }


       // 해당하는 Post가 전부 delete true라면 오류를 던진다.
       if( checkedAllPosts.isEmpty() ){
           throw new PostNotFoundException(POST_NOT_FOUND_ERROR);
       }


       // 조건에 맞는 최종 Post들을 반환한다.
       return checkedAllPosts;


    }




}
