package com.example.infrapostservice.infra;

import com.example.infrapostservice.environment.JpaIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.*;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PostRepositoryTest extends JpaIntegrationTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    PostImageRepository postImageRepository;

    @Test
    @DisplayName("UUID 조회 테스트")
    void uuid_find_test() {
        UUID id1 = UUID.fromString("619a3167-428a-43a4-b246-c0462286d962");
        UUID id2 = UUID.fromString("619a3167-428a-43a4-b246-c0462286d963");
        List<UUID> params = List.of(id1, id2);

        assertDoesNotThrow(() -> {
            List<PostEntity> result = postRepository.findAllById(params);
            assertThat(result.size()).isEqualTo(0);
        });
    }

    @Test
    @DisplayName("날짜 비교 테스트")
    void uuid_find_all_test() {
        List<PostEntity> dummy = List.of(
                PostEntity.builder()
                        .title("post1")
                        .summary("This is post1.")
                        .contents("This is post1.")
                        .createdAt(Instant.now())
                        .build()
        );

        postRepository.saveAll(dummy);

        LocalDate now = LocalDate.now();
        LocalDateTime start = now.atStartOfDay();
        LocalDateTime end = LocalDateTime.of(now, LocalTime.MAX);

        assertDoesNotThrow(() -> {
            System.out.println("start = " + start.toInstant(ZoneOffset.UTC));
            System.out.println("end = " + end.toInstant(ZoneOffset.UTC));

            List<PostEntity> result = postRepository.findAllByCreatedAtBetween(
                    start.toInstant(ZoneOffset.UTC),
                    end.toInstant(ZoneOffset.UTC),
                    Sort.by(Sort.Order.asc("createdAt"))
            );

            assertThat(result.size()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("UUID 저장 테스트")
    void uuid_save_test() {
        UUID uuid = UUID.fromString("619a3167-428a-43a4-b246-c0462286d962");
        PostImageEntity imageEntity = PostImageEntity.builder()
                .uuid(uuid)
                .name("post image1")
                .path("post/image/" + uuid)
                .build();

        PostImageEntity newImage = postImageRepository.save(imageEntity);

        PostEntity entity = PostEntity.builder()
                .title("post1")
                .summary("This is post1.")
                .contents("This is post1.")
                .images(List.of(newImage))
                .build();

        assertDoesNotThrow(() -> {
            PostEntity result = postRepository.save(entity);
            assertThat(result.getId()).isInstanceOf(UUID.class);
        });
    }
}
