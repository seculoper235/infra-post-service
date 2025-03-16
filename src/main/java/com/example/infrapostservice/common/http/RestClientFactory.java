package com.example.infrapostservice.common.http;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


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
        RestClient restClient = RestClient.create(baseUrl);

        return this.createClient(restClient, clientSpec);
    }
}
