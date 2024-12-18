package org.durcit.be.comment.repository;

import org.durcit.be.comment.domain.Comment;
import org.durcit.be.comment.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<Comment, Long> {

}
