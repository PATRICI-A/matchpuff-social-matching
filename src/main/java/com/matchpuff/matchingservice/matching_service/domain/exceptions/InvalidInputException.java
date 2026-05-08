package com.matchpuff.matchingservice.matching_service.domain.exceptions;
import org.springframework.http.HttpStatus;


public class InvalidInputException extends UserMatchProfile {

    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
