package com.example.infrapostservice.service;

import com.example.infrapostservice.common.http.RestClientFactory;
import com.example.infrapostservice.domain.FileClient;
import com.example.infrapostservice.domain.FileReference;
import com.example.infrapostservice.domain.MappingRequest;
import com.example.infrapostservice.web.exception.model.ExternalServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@Import({RestClientFactory.class})
@RestClientTest
public class FileClientTest {
    private final String BASE_URL = "http://localhost:8082/api/file";

    private final MockRestServiceServer server;

    private final FileClient fileClient;

    @Autowired
    private ObjectMapper objectMapper;

    public FileClientTest(
            @Autowired RestClient.Builder builder,
            @Autowired RestClientFactory factory
    ) {
        this.server = MockRestServiceServer.bindTo(builder).build();

        RestClient restClient = builder
                .baseUrl(BASE_URL)
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new ExternalServiceException(IOUtils.toString(response.getBody(), StandardCharsets.UTF_8));
                })
                .build();

        this.fileClient = factory.createClient(restClient, FileClient.class);
    }

    @Test
    @DisplayName("파일 매핑 요청시, 해당 파일이 존재한다면 매핑된 파일 정보를 반환한다")
    public void mapping_file_request_exist_file_return_file_reference() throws JsonProcessingException {
        List<FileReference> expected = List.of(
                new FileReference(UUID.randomUUID(), "text1", "post/image/text1")
        );
        String response = objectMapper.writeValueAsString(expected);

        server.expect(requestTo(BASE_URL + "/mapping"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        MappingRequest request = new MappingRequest(
                "post1",
                List.of(UUID.randomUUID())
        );

        List<FileReference> result = assertDoesNotThrow(() -> fileClient.mapping(request));

        assertThat(result.stream().map(FileReference::id).toList())
                .isEqualTo(expected.stream().map(FileReference::id).toList());
    }

    @Test
    @DisplayName("파일 매핑 요청시, 해당 파일이 존재하지 않는다면 404 오류를 반환한다")
    public void mapping_file_request_not_exist_file_return_404() throws JsonProcessingException {
        String expected = "404 NotFound. Not exist files.";
        String body = objectMapper.writeValueAsString(expected);

        server.expect(requestTo(BASE_URL + "/mapping"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).body(body).contentType(MediaType.APPLICATION_JSON));

        MappingRequest request = new MappingRequest(
                "post1",
                List.of(UUID.randomUUID())
        );

        assertThrows(ExternalServiceException.class,
                () -> fileClient.mapping(request)
        );
    }
}
