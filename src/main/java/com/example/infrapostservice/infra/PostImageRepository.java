package com.example.infrapostservice.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImageEntity, Long> {
    void deleteAllByPost(PostEntity post);
}
