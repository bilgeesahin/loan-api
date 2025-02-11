package com.trial.loan_api.advice;

import com.trial.loan_api.dto.response.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<SimpleResponse> handleInternalServerError(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .internalServerError()
                .body(SimpleResponse.fail(ex.getMessage()));
    }
}
