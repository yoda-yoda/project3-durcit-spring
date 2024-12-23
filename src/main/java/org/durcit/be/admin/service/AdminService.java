package org.durcit.be.admin.service;

import org.durcit.be.admin.dto.AdminLogResponse;
import org.durcit.be.post.dto.PostCardResponse;

import java.util.List;

public interface AdminService {

    public List<AdminLogResponse> getAllLogs();
    public PostCardResponse recoverPostAndPostsTag(Long postId);

}
