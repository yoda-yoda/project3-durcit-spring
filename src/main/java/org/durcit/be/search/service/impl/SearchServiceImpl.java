package org.durcit.be.search.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.SearchRequest;
import org.durcit.be.search.repository.SearchRepository;
import org.durcit.be.post.repository.PostRepository;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.search.dto.SearchResultResponse;
import org.durcit.be.search.service.SearchService;
import org.durcit.be.security.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostsTagRepository postsTagRepository;
    private final SearchRepository searchRepository;

    public List<SearchResultResponse> search(String query) {
        List<SearchResultResponse> results = new ArrayList<>();

        results.addAll(postRepository.findByTitleContaining(query).stream()
                .map(post -> new SearchResultResponse("post", post.getId(), post.getTitle()))
                .toList());

        results.addAll(memberRepository.findByNicknameContaining(query).stream()
                .map(user -> new SearchResultResponse("user", user.getId(), user.getNickname()))
                .toList());

        results.addAll(postsTagRepository.findByContentsContaining(query).stream()
                .map(tag -> new SearchResultResponse("tag", tag.getId(), tag.getContents()))
                .toList());

        return results;

    }






    // 메서드 기능: 검색한 Post를 찾아온다. 검색내용이 제목, 내용, 태그, 닉네임에 포함되는지를 찾고 delete가 false인 Post를 찾는다.
    // 태그와 Post가, delete false인 것만 찾는다.
    // 예외 X: 즉 해당하는 것이 없거나, delete가 true면 예외를 던지지않고, 최종적으로 빈 List를 반환하도록 한다.
    // 반환: 응답 Dto로 변환해 List로 담아 최종 반환한다.
    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeleted(SearchRequest searchRequest) {


        // Dto의 getter를 통해, 검색된 내용을 뽑는다.
        String search = searchRequest.getSearch();


        // 리포지터리에서 jpql로 만든 메서드를 활용해 조회한다.
        // 즉 검색 내용이 포함된 제목, 내용, 태그, 닉네임의 Post를 찾은후 delete false 만 DB에서 조회하여 담는다.
        List<Post> findPostList = searchRepository.findCheckedAllPostWithNonDeleted(search);


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
