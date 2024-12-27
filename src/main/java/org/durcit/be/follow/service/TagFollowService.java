package org.durcit.be.follow.service;

import org.durcit.be.follow.domain.TagFollow;
import org.durcit.be.follow.dto.TagFollowRegisterRequest;
import org.durcit.be.follow.dto.TagFollowResponse;
import org.durcit.be.follow.dto.TagFollowUpdateRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;

import java.util.List;


public interface TagFollowService {

   public List<PostsTagResponse> processTagsWithFollowStatus(List<PostsTagResponse> tags, Long memberId);
   public TagFollowResponse createAndDeleteTagFollowByRegisterRequest(TagFollowRegisterRequest tagFollowRegisterRequest, Long memberId);
   public List<TagFollowResponse> createTagFollowsByRegisterRequests(List<TagFollowRegisterRequest> tagFollowRegisterRequestList, Long memberId);
   public List<TagFollowResponse> createTagFollowsByUpdateRequests(List<TagFollowUpdateRequest> tagFollowUpdateRequestList, Long memberId);
   public List<TagFollow> getAllTagFollowEntitys() ;
   public List<TagFollowResponse> getAllTagFollowResponses() ;
   public List<TagFollow> getNoneDeletedTagFollows(Long memberId) ;
   public List<TagFollowResponse> getTagFollowsResponses(Long memberId) ;
   public List<TagFollowResponse> updateTagFollows(List<TagFollowUpdateRequest> tagFollowUpdateRequestList, Long memberId );
   public void deleteNoneDeletedAllTagFollows(Long memberId) ;
   public void deleteAllTagFollowsByIds(List<Long> tagFollowIdList) ;
   public void deleteOneTagFollowById(Long tagFollowId);




}
