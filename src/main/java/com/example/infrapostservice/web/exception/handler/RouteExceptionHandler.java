package com.example.infrapostservice.web.exception.handler;

import com.example.infrapostservice.web.exception.model.EntityNotFoundException;
import com.example.infrapostservice.web.exception.model.ExceptionStatus;
import com.example.infrapostservice.web.exception.model.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestControllerAdvice
public class RouteExceptionHandler {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {

        ExceptionResponse response = new ExceptionResponse(
                dateFormat.format(new Date()),
                ExceptionStatus.PS001,
                e.getMessage());

        log.error(response.getDetail());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ExceptionResponse> handleExternalServiceException(ExternalServiceException e) {

        ExceptionResponse response = new ExceptionResponse(
                dateFormat.format(new Date()),
                ExceptionStatus.PS002,
                "요청 작업 처리중 에러가 발생하였습니다");

        log.error(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
