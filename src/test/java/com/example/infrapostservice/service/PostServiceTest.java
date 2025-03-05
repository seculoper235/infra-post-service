package com.example.infrapostservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Test
    @DisplayName("포스트 등록 시, 파일 매핑에 성공한다면 포스트 정보가 반환된다")
    public void create_post_success_file_mapping_return_new_post_files() {
        // UUID과 업로드 된 파일들을 포함한 포스트 정보를 가지고 등록을 진행
    }

    @Test
    @DisplayName("포스트 수정 시, 파일 매핑에 성공한다면 변경된 포스트 정보가 반환된다")
    public void update_post_success_file_mapping_return_updated_post_data() {
        // UUID와 업로드 된 파일들을 포함한 포스트 정보를 가지고 업데이트를 진행
    }

    @Test
    @DisplayName("포스트 삭제 시, 파일 데이터 삭제가 실패한다면 에러를 발생한다")
    public void delete_post_fail_delete_table_data_throw_error() {
        // UUID를 넘겨서 파일 삭제 진행
    }
}
