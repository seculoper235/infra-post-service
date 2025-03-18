package com.example.infrapostservice.common.http;

import com.example.infrapostservice.web.exception.model.ExternalServiceException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.nio.charset.StandardCharsets;


@Component
public class RestClientFactory implements ClientFactory {
    protected HttpServiceProxyFactory httpServiceProxyFactory(RestClient restClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                .build();
    }

    @Override
    public <T> T createClient(RestClient restClient, Class<T> clientSpec) {
        return httpServiceProxyFactory(restClient)
                .createClient(clientSpec);
    }

    public <T> T createClient(String baseUrl, Class<T> clientSpec) {
        RestClient restClient = RestClient.builder()
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new ExternalServiceException(IOUtils.toString(response.getBody(), StandardCharsets.UTF_8));
                })
                .baseUrl(baseUrl)
                .build();

        return this.createClient(restClient, clientSpec);
    }
}
