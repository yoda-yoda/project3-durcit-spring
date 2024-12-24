package org.durcit.be.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.durcit.be.comment.dto.CommentCardResponse;
import org.durcit.be.post.dto.EmojiResponse;
import org.durcit.be.post.dto.PostEmojisResponse;
import org.durcit.be.post.dto.PostResponse;
import org.durcit.be.postsTag.dto.PostsTagResponse;
import org.durcit.be.upload.dto.UploadResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCombinedResponse {

    private PostResponse post;
    private List<PostsTagResponse> tags;
    private List<UploadResponse> uploads;
    private PostEmojisResponse emojis;
    private List<CommentCardResponse> comments;

}
