package com.example.infrapostservice.infra;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    List<PostEntity> findAllByCreatedAtBetween(Instant createdAtAfter, Instant createdAtBefore, Sort sort);
}
