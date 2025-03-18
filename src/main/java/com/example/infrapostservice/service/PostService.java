package com.example.infrapostservice.service;

import com.example.infrapostservice.domain.FileClient;
import com.example.infrapostservice.domain.FileReference;
import com.example.infrapostservice.domain.MappingRequest;
import com.example.infrapostservice.infra.PostEntity;
import com.example.infrapostservice.infra.PostImageEntity;
import com.example.infrapostservice.infra.PostImageRepository;
import com.example.infrapostservice.infra.PostRepository;
import com.example.infrapostservice.model.PostDetail;
import com.example.infrapostservice.model.PostInfo;
import com.example.infrapostservice.web.exception.model.EntityNotFoundException;
import io.vavr.control.Either;
import io.vavr.control.Option;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FileClient fileClient;

    public List<PostInfo> find(
            Optional<LocalDate> localDate
    ) {
        LocalDate date = localDate.orElse(LocalDate.now());
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

        return postRepository.findAllByCreatedAtBetween(
                start.toInstant(ZoneOffset.UTC),
                end.toInstant(ZoneOffset.UTC),
                Sort.by(Sort.Order.asc("createdAt"))
        ).stream().map(PostEntity::toInfo).toList();
    }

    public Either<EntityNotFoundException, PostDetail> findById(
            UUID id
    ) {
        return Option.ofOptional(postRepository.findById(id))
                .map(PostEntity::toDetail)
                .toEither(new EntityNotFoundException("존재하지 않는 포스트입니다: " + id));
    }

    @Transactional
    public PostDetail register(
            String title,
            String summary,
            String contents,
            List<UUID> images
    ) {
        PostEntity postEntity = PostEntity.builder()
                .title(title)
                .summary(summary)
                .contents(contents)
                .build();

        PostEntity post = postRepository.save(postEntity);

        List<PostImageEntity> newImages = uploadImages(post, images);

        newImages.stream().findFirst()
                .ifPresent(thumbnail -> post.changeThumbnail(thumbnail.getPath()));

        log.info("포스트가 등록되었습니다: {}", post.getId());

        return post.toDetail();
    }

    @Transactional
    public Either<EntityNotFoundException, PostDetail> update(
            UUID id,
            String title,
            String summary,
            String contents,
            List<UUID> images
    ) {
        Either<EntityNotFoundException, PostEntity> postEntity = Option.ofOptional(postRepository.findById(id))
                .peek(post -> post.updatePostText(title, summary, contents))
                .toEither(new EntityNotFoundException("존재하지 않는 포스트입니다: " + id));

        return postEntity.peek(post -> {
            boolean notModify = post.getImages().stream()
                    .map(PostImageEntity::getUuid)
                    .equals(images.stream());

            if (!notModify) {
                List<PostImageEntity> updateImages = uploadImages(post, images);

                updateImages.stream().findFirst()
                        .ifPresent(thumbnail -> post.changeThumbnail(thumbnail.getPath()));
            }
        }).map(PostEntity::toDetail);
    }

    @Transactional
    protected List<PostImageEntity> uploadImages(PostEntity post, List<UUID> images) {
        MappingRequest mappingRequest = new MappingRequest(
                post.getId().toString(),
                images
        );

        List<FileReference> files = fileClient.mapping(mappingRequest);

        postImageRepository.deleteAllByPost(post);

        List<PostImageEntity> updateImages = files.stream().map(file ->
                PostImageEntity.builder()
                        .uuid(file.id())
                        .name(file.name())
                        .path(file.path())
                        .post(post)
                        .build()
        ).toList();

        List<PostImageEntity> result = postImageRepository.saveAll(updateImages);

        log.info("포스트 이미지가 등록되었습니다: rows {}", result.size());

        return result;
    }

    @Transactional
    public void delete(UUID id) {
        postRepository.deleteById(id);
        fileClient.delete(id.toString());

        log.info("포스트가 삭제되었습니다: {}", id);
    }
}
