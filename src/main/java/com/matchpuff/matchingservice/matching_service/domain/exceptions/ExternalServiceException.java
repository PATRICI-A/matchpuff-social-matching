package com.matchpuff.matchingservice.matching_service.domain.exceptions;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends UserMatchProfile {
    public ExternalServiceException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
