package org.durcit.be.postsTag.service;

import org.durcit.be.postsTag.domain.PostsTag;
import org.durcit.be.postsTag.dto.PostsTagRegisterRequest;
import org.durcit.be.postsTag.dto.PostsTagResponse;

import java.util.List;

public interface PostsTagService {

    public List<PostsTagResponse> createPostsTag(List<PostsTagRegisterRequest> postsTagRegisterRequestList, Long postId);
    public List<PostsTag> getAllPostsTagsWithNonDeleted();
    public List<PostsTag> getAllPostsTags();
    public PostsTag getPostsTagById(Long postsTagId);
    public List<PostsTag> getPostsTagListByPostId(Long postId);
    public List<PostsTag> getNoneDeletedPostsTagListByPostId(Long postId);
    public List<PostsTagResponse> getPostsTagResponseListByPostId(Long postId);
    public List<PostsTag> getPostsTagByContents(PostsTagRegisterRequest postsTagRegisterRequest);
    public List<PostsTagResponse> updatePostsTag(List<PostsTagRegisterRequest> postsTagRegisterRequestList, Long postId);
    public void deleteOnePostsTagByPostsTagId(Long postsTagId);
    public void deletePostsTagsByPostsTagId(List<Long> postsTagIdList);
    public void deletePostsTagsByPostId(Long postId);



}
