package org.durcit.be.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.PostsContentSearchRequest;
import org.durcit.be.search.dto.PostsSearchRequest;
import org.durcit.be.search.repository.PostsContentSearchRepository;
import org.durcit.be.search.repository.PostsSearchRepository;
import org.durcit.be.search.service.PostsContentSearchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsContentSearchServiceImpl implements PostsContentSearchService {


    private final PostsContentSearchRepository postsContentSearchRepository;


    // 메서드 기능: 검색한 Post 내용을 포함하면서 동시에 delete false 인 Post만 찾아담는다.
    // 예외 X: 즉 해당하는 내용이 없거나, delete가 true면 예외를 던지지않고, 최종적으로 빈 List를 반환하도록 한다.
    // 반환: 응답 Dto로 변환해 List로 담아 최종 반환한다.
    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeleted(PostsContentSearchRequest postsContentSearchRequest) {


        // Dto의 getter를 통해, 검색된 Post 내용을 뽑는다.
        String postContent = postsContentSearchRequest.getContent();


        // 리포지터리에서 jpql로 만든 메서드를 활용해 조회한다.
        // 즉 검색 내용이 포함된 내용의 Post와, delete false 만 DB에서 조회하여 담는다.
        List<Post> findPostList = postsContentSearchRepository.findPostsByContentAndNoneDeleted(postContent);


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



}
