package com.example.infrapostservice.infra;

import com.example.infrapostservice.model.PostDetail;
import com.example.infrapostservice.model.PostInfo;
import io.vavr.control.Option;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"POST\"")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 100)
    private String thumbnail;

    @Column(nullable = false, length = 3000)
    private String contents;

    @Builder.Default
    @Column(nullable = false)
    @OneToMany(mappedBy = "post")
    private List<PostImageEntity> images = Collections.emptyList();

    @CreationTimestamp
    @Builder.Default
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @UpdateTimestamp
    @Builder.Default
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    public void updatePostText(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void updateImages(List<PostImageEntity> images) {
        this.images = images;
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public PostInfo toInfo() {
        return new PostInfo(
                id.toString(),
                title,
                Option.of(thumbnail),
                contents.substring(0, 50),
                createdAt
        );
    }

    public PostDetail toDetail() {
        return new PostDetail(
                id.toString(),
                title,
                contents,
                images.stream().map(PostImageEntity::toModel).toList(),
                createdAt
        );
    }
}
