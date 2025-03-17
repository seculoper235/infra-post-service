package com.example.infrapostservice.service;

import com.example.infrapostservice.domain.FileClient;
import com.example.infrapostservice.domain.FileReference;
import com.example.infrapostservice.infra.PostEntity;
import com.example.infrapostservice.infra.PostImageEntity;
import com.example.infrapostservice.infra.PostImageRepository;
import com.example.infrapostservice.infra.PostRepository;
import com.example.infrapostservice.model.PostDetail;
import com.example.infrapostservice.web.PostUpdateRequest;
import com.example.infrapostservice.web.exception.model.EntityNotFoundException;
import com.example.infrapostservice.web.exception.model.ExternalServiceException;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    private final PostRepository postRepository = mock(PostRepository.class);
    private final PostImageRepository imageRepository = mock(PostImageRepository.class);
    private final FileClient fileClient = mock(FileClient.class);

    @Test
    @DisplayName("포스트 등록 시, 파일 매핑에 성공한다면 포스트 정보가 반환된다")
    public void create_post_success_file_mapping_return_new_post_files() {
        List<PostImageEntity> expectedImages = Collections.emptyList();
        List<FileReference> expectedMappings = Collections.emptyList();

        PostEntity expected = PostEntity.builder()
                .id(UUID.randomUUID())
                .title("post1")
                .contents("This is post1.")
                .images(expectedImages)
                .build();

        PostUpdateRequest request = new PostUpdateRequest(
                UUID.randomUUID(),
                "post1",
                "This is updated post.",
                Collections.emptyList()
        );

        given(postRepository.save(any())).willReturn(expected);
        given(fileClient.mapping(any())).willReturn(expectedMappings);
        doNothing().when(imageRepository).deleteAllByPost(any());
        given(imageRepository.saveAll(any())).willReturn(expectedImages);

        assertDoesNotThrow(() -> {
            postService.register(
                    request.title(),
                    request.contents(),
                    request.images().stream().map(UUID::fromString).toList()
            );
        });
    }

    @Test
    @DisplayName("포스트 수정 시, 해당하는 포스트가 없다면 에러를 던진다")
    public void update_post_not_exist_post_throw_entity_not_found_exception() {
        PostUpdateRequest request = new PostUpdateRequest(
                UUID.randomUUID(),
                "post1",
                "This is updated post.",
                Collections.emptyList()
        );

        given(postRepository.findById(any())).willReturn(Optional.empty());

        Either<EntityNotFoundException, PostDetail> result = postService.update(
                request.id(),
                request.title(),
                request.contents(),
                request.images().stream().map(UUID::fromString).toList()
        );

        assertTrue(result.isLeft());
        assertThrows(EntityNotFoundException.class,
                () -> result.getOrElseThrow(it -> it)
        );
    }

    @Test
    @DisplayName("포스트 수정 시, 파일 매핑에 실패한다면 에러를 던진다")
    public void update_post_fail_file_throw_external_service_exception() {
        Optional<PostEntity> expected = Optional.of(PostEntity.builder()
                .id(UUID.randomUUID())
                .title("post1")
                .contents("This is post1.")
                .build());

        List<PostImageEntity> images = List.of(
                PostImageEntity.builder()
                        .id(1L)
                        .uuid(UUID.randomUUID())
                        .name("post image1")
                        .path("post/image/uuid")
                        .post(expected.get())
                        .build()
        );

        PostUpdateRequest request = new PostUpdateRequest(
                UUID.randomUUID(),
                "post1",
                "This is updated post.",
                images.stream().map(image -> image.getUuid().toString()).toList()
        );

        given(postRepository.findById(any())).willReturn(expected);
        given(fileClient.mapping(any())).willThrow(ExternalServiceException.class);

        assertThrows(ExternalServiceException.class,
                () -> postService.update(
                        request.id(),
                        request.title(),
                        request.contents(),
                        request.images().stream().map(UUID::fromString).toList()
                )
        );
    }

    @Test
    @DisplayName("포스트 수정 시, 파일 매핑에 성공한다면 변경된 포스트 정보가 반환된다")
    public void update_post_success_file_mapping_return_updated_post_data() {
        List<FileReference> files = Collections.emptyList();

        Optional<PostEntity> expected = Optional.of(PostEntity.builder()
                .id(UUID.randomUUID())
                .title("post1")
                .contents("This is post1.")
                .images(Collections.emptyList())
                .build());

        PostUpdateRequest request = new PostUpdateRequest(
                UUID.randomUUID(),
                "post1",
                "This is updated post.",
                Collections.emptyList()
        );

        given(fileClient.mapping(any())).willReturn(files);
        given(postRepository.findById(any())).willReturn(expected);

        Either<EntityNotFoundException, PostDetail> result = postService.update(
                request.id(),
                request.title(),
                request.contents(),
                request.images().stream().map(UUID::fromString).toList()
        );

        assertTrue(result.isRight());
    }

    @Test
    @DisplayName("포스트 삭제 시, 파일 데이터 삭제가 실패한다면 에러를 던진다")
    public void delete_post_fail_delete_table_data_throw_error() {
        UUID id = UUID.randomUUID();
        Mockito.doThrow(ExternalServiceException.class).when(fileClient).delete(any());

        assertThrows(ExternalServiceException.class,
                () -> postService.delete(id));
    }
}
