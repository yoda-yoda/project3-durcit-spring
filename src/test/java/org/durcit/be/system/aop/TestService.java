package org.durcit.be.system.aop;

import org.durcit.be.post.aop.annotations.PostRequireAuthorization;
import org.durcit.be.post.aop.annotations.RequireCurrentMemberId;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @RequireCurrentMemberId
    public void sampleMethod() {
        // Simulated method for AOP testing
    }

    @PostRequireAuthorization
    public void updatePost(Long postId) {
        // Simulated method for AOP testing
    }

}
