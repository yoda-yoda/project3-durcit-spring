package org.durcit.be.push.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.push.domain.Push;
import org.durcit.be.push.dto.PushResponse;
import org.durcit.be.push.repository.PushRepository;
import org.durcit.be.push.service.PushService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PushServiceImpl implements PushService {

    private final PushRepository pushRepository;

    public void createPush(Push push) {
        pushRepository.save(push);
    }

    public List<PushResponse> getPushsByMemberId(String memberId) {
        List<Push> pushList = pushRepository.findAllByMemberId(memberId);
        return pushList.stream()
                .map(push -> PushResponse.builder()
                        .id(push.getId())
                        .message(push.getContent())
                        .createdAt(push.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }


}
