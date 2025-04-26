package com.project.donate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotActiveException extends RuntimeException {
    public ResourceNotActiveException(String message) {
        super(message);
    }
}
