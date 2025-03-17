package com.example.infrapostservice.infra;

import com.example.infrapostservice.model.PostImage;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "\"POST_IMAGE\"")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 100)
    private String path;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public PostImage toModel() {
        return new PostImage(
                id.toString(),
                path
        );
    }
}
