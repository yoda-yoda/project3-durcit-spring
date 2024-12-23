package org.durcit.be.comment.repository;

import org.durcit.be.comment.domain.Comment;
import org.durcit.be.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndDeletedFalse(Long id);

    List<Comment> findAllByPostIdAndDeletedFalse(Long postId);

    List<Comment> findAllByAuthorIdAndDeletedFalse(Long authorId);
}
