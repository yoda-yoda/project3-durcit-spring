package org.durcit.be.push.service;

import org.durcit.be.push.domain.Push;
import org.durcit.be.push.dto.PushResponse;

import java.util.List;

public interface PushService {
    public void createPush(Push push);
    public List<PushResponse> getPushsByMemberId(String memberId);
}
