package org.durcit.be.upload.repository;

import org.durcit.be.upload.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagesRepository extends JpaRepository<Images, Long> {
}
