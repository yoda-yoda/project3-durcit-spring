package org.durcit.be.push.service;

import org.durcit.be.push.domain.Push;
import org.durcit.be.push.dto.PushResponse;

import java.util.List;

public interface PushService {
    public Push createPush(Push push);
    public List<PushResponse> getPushsByMemberId(String memberId);
    public void confirmPush(Long pushId);
}
