package com.example.infrapostservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileClientTest {
    @Test
    @DisplayName("파일 매핑 요청시, 해당 파일이 존재한다면 매핑된 파일 정보를 반환한다")
    public void mapping_file_request_exist_file_return_file_reference() {
        //
    }

    @Test
    @DisplayName("파일 매핑 요청시, 해당 파일이 존재하지 않는다면 404 오류를 반환한다")
    public void mapping_file_request_not_exist_file_return_404() {
        //
    }
}
