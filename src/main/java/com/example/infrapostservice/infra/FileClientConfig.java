package com.example.infrapostservice.infra;

import com.example.infrapostservice.common.http.RestClientFactory;
import com.example.infrapostservice.domain.FileClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FileClientConfig {
    @Value("${file.service.baseUrl}")
    private String baseUrl;

    private final RestClientFactory abstractRestClientFactory;

    @Bean
    public FileClient fileClient() {
        return abstractRestClientFactory.createClient(baseUrl, FileClient.class);
    }
}
