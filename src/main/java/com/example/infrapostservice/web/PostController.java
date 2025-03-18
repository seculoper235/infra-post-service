package com.example.infrapostservice.web;

import com.example.infrapostservice.model.PostDetail;
import com.example.infrapostservice.model.PostInfo;
import com.example.infrapostservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> register(
            @Valid @RequestBody PostCreateRequest request
    ) {
        PostDetail result = postService.register(
                request.title(),
                request.summary(),
                request.contents(),
                request.images()
        );

        return ResponseEntity.created(URI.create("/api/post/" + result.id())).build();
    }

    @GetMapping
    public ResponseEntity<List<PostInfo>> find(
            @RequestParam LocalDate date
    ) {
        List<PostInfo> result = postService.find(Optional.of(date));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetail> findById(@PathVariable UUID id) {
        PostDetail result = postService.findById(id)
                .getOrElseThrow(it -> it);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDetail> update(
            @PathVariable UUID id,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        PostDetail result = postService.update(
                id,
                request.title(),
                request.summary(),
                request.contents(),
                request.images()
        ).getOrElseThrow(it -> it);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
