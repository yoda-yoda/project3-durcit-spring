package org.durcit.be.comment.service;

import org.durcit.be.security.domian.Member;

public interface MentionNotificationService {

    public void notifyToTargetMember(Member member, Member targetMember, Long postId);

}
