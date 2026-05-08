package com.matchpuff.matchingservice.matching_service.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UserMatchProfile extends RuntimeException {
    private final HttpStatus status;
    public UserMatchProfile(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
