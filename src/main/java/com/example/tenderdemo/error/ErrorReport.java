package com.example.tenderdemo.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class ErrorReport {

    public static final ErrorReport UNKNOWN_ERROR = new ErrorReport(500, "11.0.1", "Internal server error");
    public static final ErrorReport BAD_REQUEST = new ErrorReport(400, "11.0.2", "Bad request");
    public static final ErrorReport NOT_FOUND = new ErrorReport(404, "11.0.2", "Not Found");

    @JsonIgnore
    private final HttpStatus httpStatus;

    @JsonProperty
    private final String code;

    @JsonProperty
    private final String message;

    @JsonProperty
    private final List<Detail> details;

    private ErrorReport(int httpStatus, String code, String message) {
        this.httpStatus = HttpStatus.valueOf(httpStatus);
        this.code = code;
        this.message = message;
        this.details = new ArrayList<>();
    }

    private ErrorReport(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.details = new ArrayList<>();
    }

    private ErrorReport(HttpStatus status, String code, String message, List<Detail> details) {
        this.httpStatus = status;
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public ErrorReport withDetails(List<Detail> details) {
        return new ErrorReport(this.httpStatus, this.code, this.message, details);
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public static class Detail {

        @JsonProperty
        private final String code;

        @JsonProperty
        private final String message;

        public Detail(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
