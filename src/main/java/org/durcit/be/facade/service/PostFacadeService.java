package org.durcit.be.facade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.post.service.LikeService;
import org.durcit.be.post.service.PostService;
import org.durcit.be.upload.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostFacadeService {

    private final PostService postService;
    private final UploadService uploadService;
    // 태그 서비스

    // 서비스 레이어들의 결합도를 낮추기 위해 사용
    // uploadService -> 파일과 json을 같이 다루기 어렵기 때문에 따로 요청
    // postService, postTagService -> 조합 Dto 사용하여 facade 패턴으로 해당 서비스에서 같이 처리




}
