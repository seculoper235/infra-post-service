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
    @DisplayName("포스트 조회 시, 날짜가 입력되지 않으면 오늘 기준으로 조회된다")
    public void find_post_no_date_return_posts_with_now() {
        //
    }

    @Test
    @DisplayName("포스트 수정 시, 파일 매핑에 성공한다면 변경된 포스트 정보가 반환된다")
    public void update_post_return_updated_post_200() {
        //
    }

    @Test
    @DisplayName("포스트 삭제 시, 파일 삭제에 실패한다면 에러를 던진다")
    public void delete_post_fail_delete_file_throw_error() {
        //
    }
}
