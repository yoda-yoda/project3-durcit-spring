package org.durcit.be.chat.service;

import org.durcit.be.security.domian.Member;

public interface ChatNotificationService {

    public void notifyToTargetMember(Member member, Member targetMember);
}
