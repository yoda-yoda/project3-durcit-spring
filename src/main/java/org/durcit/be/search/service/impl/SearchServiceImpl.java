package org.durcit.be.search.service.impl;

import lombok.RequiredArgsConstructor;
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
public class SearchServiceImpl implements SearchService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostsTagRepository postsTagRepository;

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

}
