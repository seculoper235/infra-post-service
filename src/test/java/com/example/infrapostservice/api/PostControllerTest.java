package com.example.infrapostservice.api;

import com.example.infrapostservice.service.PostService;
import com.example.infrapostservice.web.PostController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
    @DisplayName("포스트 등록 시, 파일 매핑에 성공한다면 요청 API가 Location 헤더에 반환된다")
    public void create_post_success_file_mapping_return_post_api_201() {
        //
    }

    @Test
    @DisplayName("포스트 단일 조회 시, 해당하는 포스트가 존재하지 않으면 404 에러를 반환한다")
    public void find_post_not_exist_return_404() {
        //
    }

    @Test
    @DisplayName("포스트 수정 시, 해당하는 포스트가 존재하지 않으면 404 에러를 반환한다")
    public void update_post_not_exist_return_404() {
        //
    }

    @Test
    @DisplayName("포스트 수정 시, 파일 매핑에 실패한다면 500 에러를 반환한다")
    public void update_post_fail_file_mapping_return_500() {
        //
    }

    @Test
    @DisplayName("포스트 삭제 시, 파일 삭제에 실패한다면 500 에러를 반환한다")
    public void delete_post_fail_delete_file_return_500() {
        //
    }
}
