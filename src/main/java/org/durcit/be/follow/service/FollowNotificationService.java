package org.durcit.be.follow.service;

import org.durcit.be.security.domian.Member;

public interface FollowNotificationService {

    public void notifyToFollowee(Member follower, Long followeeId);
}
