package com.matchpuff.matchingservice.matching_service.domain.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends UserMatchProfile {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
