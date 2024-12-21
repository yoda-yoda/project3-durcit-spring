package org.durcit.be.push.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.durcit.be.push.domain.Push;
import org.durcit.be.push.dto.PushResponse;
import org.durcit.be.push.repository.PushRepository;
import org.durcit.be.push.service.PushService;
import org.durcit.be.system.exception.push.PushNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.durcit.be.system.exception.ExceptionMessage.PUSH_CANNOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PushServiceImpl implements PushService {

    private final PushRepository pushRepository;

    @Transactional
    public Push createPush(Push push) {
        return pushRepository.save(push);
    }

    public List<PushResponse> getPushsByMemberId(String memberId) {
        List<Push> pushList = pushRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);
        return pushList.stream()
                .map(push -> PushResponse.builder()
                        .id(push.getId())
                        .message(push.getContent())
                        .createdAt(push.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void confirmPush(Long pushId) {
        Push push = pushRepository.findById(pushId)
                .orElseThrow(() -> new PushNotFoundException(PUSH_CANNOT_FOUND_ERROR));
        push.setConfirmed(true);
        pushRepository.save(push);
    }


}
