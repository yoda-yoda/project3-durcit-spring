package org.durcit.be.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.durcit.be.comment.dto.MentionResponse;
import org.durcit.be.comment.service.MentionService;
import org.durcit.be.security.domian.Member;
import org.durcit.be.security.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentionServiceImpl implements MentionService {

    private final MemberRepository memberRepository;

    public List<MentionResponse> getMentionableMembers(String query) {
        List<Member> members = memberRepository.findByNicknameContainingIgnoreCaseAndIsBlockedFalse(query);

        return members.stream()
                .map(member -> MentionResponse.builder()
                        .id(member.getId())
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImage())
                        .build())
                .toList();
    }

}
