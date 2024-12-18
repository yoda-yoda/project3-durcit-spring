package org.durcit.be.postsTag.service;

import lombok.RequiredArgsConstructor;
import org.durcit.be.postsTag.domain.Posts;
import org.durcit.be.postsTag.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostsService { // 이 클래스는 PostsTag 기능을 만들어 테스트하기위해 대충만든 포스트서비스 계층이다.

    private final PostsRepository postsRepository;

    public Posts save(Posts posts) {
        return postsRepository.save(posts);
    }

    public List<Posts> findAll() {
       return postsRepository.findAll();
    }

    public Posts findOne(Long id) {
        Optional<Posts> byId = postsRepository.findById(id);
        if (byId.isPresent()) {
           return byId.get();
        }
        throw new RuntimeException("findById로 찾은 포스트 옵셔널이 null 입니다.");
    }

    public void delete(Long id) {
        Posts one = findOne(id);
        one.setDeleted(true);
    }

}
