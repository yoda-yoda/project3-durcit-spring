package org.durcit.be.comment.service;

import org.durcit.be.comment.dto.MentionResponse;

import java.util.List;

public interface MentionService {

    public List<MentionResponse> getMentionableMembers(String query);

}
