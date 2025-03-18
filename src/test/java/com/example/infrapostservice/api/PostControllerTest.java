package com.example.infrapostservice.api;

import com.example.infrapostservice.model.PostDetail;
import com.example.infrapostservice.service.PostService;
import com.example.infrapostservice.web.PostController;
import com.example.infrapostservice.web.PostCreateRequest;
import com.example.infrapostservice.web.PostUpdateRequest;
import com.example.infrapostservice.web.exception.model.EntityNotFoundException;
import com.example.infrapostservice.web.exception.model.ExceptionStatus;
import com.example.infrapostservice.web.exception.model.ExternalServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(value = PostController.class)
public class PostControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DisplayName("포스트 등록 시, 잘못된 요청을 보낼 경우 400 에러를 반환한다")
    public void create_post_bad_request_return_400() throws Exception {
        String summary = "Lorem Ipsum is simply dummy text of the printing and typesetting industry." +
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

        PostCreateRequest request = new PostCreateRequest(
                "title",
                summary,
                "contents",
                Collections.emptyList()
        );

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ExceptionStatus.PS003.name()))
        ;
    }

    @Test
    @DisplayName("포스트 등록 시, 파일 매핑에 성공한다면 요청 API가 Location 헤더에 반환된다")
    public void create_post_success_file_mapping_return_post_api_201() throws Exception {
        PostCreateRequest request = new PostCreateRequest(
                "title",
                "summary",
                "contents",
                Collections.emptyList()
        );

        PostDetail expected = new PostDetail(
                UUID.randomUUID(),
                request.title(),
                request.contents(),
                Collections.emptyList(),
                Instant.now()
        );
        given(postService.register(any(), any(), any(), any()))
                .willReturn(expected);

        mockMvc.perform(post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/post/" + expected.id()))
        ;
    }

    @Test
    @DisplayName("포스트 여러건 조회 시, 잘못된 날짜값을 넘기면 400 에러를 반환한다")
    public void find_post_all_invalid_date_return_400() throws Exception {
        String invalid = "20252124";

        mockMvc.perform(get("/api/post")
                        .queryParam("date", invalid)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ExceptionStatus.PS003.name()))
        ;
    }

    @Test
    @DisplayName("포스트 여러건 조회 시, 조회에 성공한다면 200 응답을 반환한다")
    public void find_post_all_success_return_200() throws Exception {
        LocalDate date = LocalDate.now();

        given(postService.find(any()))
                .willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/post")
                        .queryParam("date", date.toString())
                ).andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("포스트 단일 조회 시, 해당하는 포스트가 존재하지 않으면 404 에러를 반환한다")
    public void find_post_not_exist_return_404() throws Exception {
        UUID id = UUID.randomUUID();

        given(postService.findById(any()))
                .willThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/post/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ExceptionStatus.PS001.name()))
        ;
    }

    @Test
    @DisplayName("포스트 수정 시, 해당하는 포스트가 존재하지 않으면 404 에러를 반환한다")
    public void update_post_not_exist_return_404() throws Exception {
        UUID id = UUID.randomUUID();

        PostUpdateRequest request = new PostUpdateRequest(
                id,
                "title",
                "summary",
                "contents",
                Collections.emptyList()
        );

        given(postService.update(any(), any(), any(), any(), any()))
                .willReturn(Either.left(new EntityNotFoundException("")));

        mockMvc.perform(put("/api/post/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ExceptionStatus.PS001.name()))
        ;
    }

    @Test
    @DisplayName("포스트 수정 시, 파일 매핑에 실패한다면 500 에러를 반환한다")
    public void update_post_fail_file_mapping_return_500() throws Exception {
        UUID id = UUID.randomUUID();

        PostUpdateRequest request = new PostUpdateRequest(
                id,
                "title",
                "summary",
                "contents",
                Collections.emptyList()
        );

        given(postService.update(any(), any(), any(), any(), any()))
                .willThrow(ExternalServiceException.class);

        mockMvc.perform(put("/api/post/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ExceptionStatus.PS002.name()))
        ;
    }

    @Test
    @DisplayName("포스트 삭제 시, 파일 삭제에 실패한다면 500 에러를 반환한다")
    public void delete_post_fail_delete_file_return_500() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(ExternalServiceException.class)
                .when(postService).delete(any());

        mockMvc.perform(delete("/api/post/{id}", id))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ExceptionStatus.PS002.name()))
        ;
    }
}
