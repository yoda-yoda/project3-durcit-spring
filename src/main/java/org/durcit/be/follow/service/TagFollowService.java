package org.durcit.be.follow.service;

import lombok.RequiredArgsConstructor;
import org.durcit.be.follow.repository.TagFollowRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagFollowService {

    private final TagFollowRepository tagFollowRepository;


}
