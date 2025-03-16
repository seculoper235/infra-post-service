package com.example.infrapostservice.common.http;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public interface ClientFactory {
    /**
     * Client Bean 생성</br>
     *
     * @param restClient {@link org.springframework.web.client.RestClient}
     * @param clientSpec {@link org.springframework.web.service.annotation.HttpExchange}로 작성한 인터페이스
     */
    <T> T createClient(RestClient restClient, Class<T> clientSpec);
}
