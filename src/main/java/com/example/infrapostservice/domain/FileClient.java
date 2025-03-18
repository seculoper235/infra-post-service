package com.example.infrapostservice.domain;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

@Component
@HttpExchange
public interface FileClient {
    @PostExchange(url = "/mapping")
    List<FileReference> mapping(
            @RequestBody MappingRequest mappingRequest);

    @DeleteExchange
    void delete(
            @RequestParam(value = "mapping") String mapping);
}
