package org.durcit.be.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.PostsSearchRequest;
import org.durcit.be.search.repository.PostsSearchRepository;
import org.durcit.be.search.service.PostsSearchService;
import org.durcit.be.system.exception.postSearch.PostSearchNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.durcit.be.system.exception.ExceptionMessage.POST_SEARCH_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsSearchServiceImpl implements PostsSearchService {


    private final PostsSearchRepository postsSearchRepository;



    // 메서드 기능: 검색한 Post 제목을 포함하면서 동시에 delete false 인 Post만 찾아담는다.
    // 예외 X: 즉 해당하는 제목이 없거나, delete가 true면 예외를 던지지않고, 최종적으로 빈 List를 반환하도록 한다.
    // 반환: 응답 Dto로 변환해 List로 담아 최종 반환한다.
    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeleted(PostsSearchRequest postsSearchRequest) {


        // Dto의 getter를 통해, 검색된 Post 제목을 뽑는다.
        String postTitle = postsSearchRequest.getTitle();


        // 리포지터리에서 jpql로 만든 메서드를 활용해 조회한다.
        // 즉 검색 내용이 포함된 제목의 Post와, delete false 만 DB에서 조회하여 담는다.
        List<Post> findPostList = postsSearchRepository.findPostsByTitleAndNoneDeleted(postTitle);


        // 그러한 Post가 없거나, delete가 true만 있다면 빈 List를 반환하고 메서드를 종료한다.
        if ( findPostList.isEmpty() ) {
            return new ArrayList<>();
        }


        // 최종 응답 List를 담을 곳을 미리 만든다.
        List<PostCardResponse> postCardResponses = new ArrayList<>();


        // 반복자를 활용해, 찾은 각각의 엔티티들을 전부 응답Dto로 바꾸고 반환할 List에 add한다.
        for (Post post : findPostList) {
            postCardResponses.add(PostCardResponse.fromEntity(post));
        }

        // 최종 응답 List를 반환한다.
        return postCardResponses;

    }



    // 메서드 기능: 검색한 Post 제목과 일치하면서 동시에 delete false 인 Post만 찾아담는다. 위 메서드와 비슷한데 예외와 반환만 다르다.
    // 예외 : 해당하는 제목이 없거나, delete가 true면 예외를 던진다.
    // 반환: 해당 Post 를 List로 담아 최종 반환한다.
    public List<Post> getAllPostsWithNoneDeleted(PostsSearchRequest postsSearchRequest) {


        // Dto의 getter를 통해, 검색된 Post 제목을 뽑는다.
        String postTitle = postsSearchRequest.getTitle();


        // 리포지터리에서 jqpl로 만든 메서드를 활용해 조회한다.
        // 즉 검색 내용이 포함된 제목의 Post와, delete false 만 DB에서 조회하여 담는다.
        List<Post> findPostList = postsSearchRepository.findPostsByTitleAndNoneDeleted(postTitle);


        // 그러한 Post가 없거나, delete true만 있다면 오류를 던진다.
        if ( findPostList.isEmpty() ) {
            throw new PostSearchNotFoundException(POST_SEARCH_NOT_FOUND_ERROR);
        }

        // 최종 응답을 반환한다.
        return findPostList;

    }




}
