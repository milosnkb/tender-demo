package com.example.tenderdemo.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    private final static Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(TenderServiceException.class)
    protected ResponseEntity<ErrorReport> handleDefaultException(TenderServiceException ex) {
        return new ResponseEntity<>(ex.getErrorReport(), ex.getErrorReport().getHttpStatus());
    }
}
