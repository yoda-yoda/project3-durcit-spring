package org.durcit.be.postsTag.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.repository.PostTagRepository;
import org.durcit.be.postsTag.repository.PostsTagRepository;
import org.durcit.be.system.exception.ExceptionMessage;
import org.durcit.be.system.exception.tag.OptionalEmptyPostsTagFindByIdException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.durcit.be.system.exception.ExceptionMessage.OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostsTagService {

    private final PostsTagRepository postTagRepository;

    @Transactional
    public PostsTag save(PostsTag postsTag) { // dto 객체로 받아서 PostTagRegisterRequest 를 만들고 이걸로 받아야한다. 구현하기.
        return postTagRepository.save(postsTag);
    }

    // 멤버아이디, 포스트아이디, 태그내용
    

    // 되도록 익셉션은 전부 service
    public PostsTag findById(Long id) {
        return postTagRepository.findById(id) // optional
                .filter(tag -> !tag.isDeleted()) //겟 가져올때 이거.
                .orElseThrow(() -> new OptionalEmptyPostsTagFindByIdException(OPTIONAL_EMPTY_POSTS_TAG_FIND_BY_ID_ERROR));
    }

    // 삭제를 remove 를 안쓰고 setDeleted true 로 할것이다.


}
