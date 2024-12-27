package org.durcit.be.search.service;
import org.durcit.be.post.domain.Post;
import org.durcit.be.post.dto.PostCardResponse;
import org.durcit.be.search.dto.PostsSearchRequest;
import java.util.List;


public interface PostsSearchService {

    public List<PostCardResponse> getAllPostCardResponsesWithNoneDeleted(PostsSearchRequest postsSearchRequest);
    public List<Post> getAllPostsWithNoneDeleted(PostsSearchRequest postsSearchRequest);


}
