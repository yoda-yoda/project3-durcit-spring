package org.durcit.be.upload.repository;

import org.durcit.be.upload.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagesRepository extends JpaRepository<Images, Long> {
    List<Images> findAllByPostId(Long postId);
}
