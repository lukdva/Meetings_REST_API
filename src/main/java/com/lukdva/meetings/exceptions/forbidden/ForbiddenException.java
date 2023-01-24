package com.lukdva.meetings.exceptions.forbidden;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class ForbiddenException extends RuntimeException{
    @Getter
    private final HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public ForbiddenException(String message) {
        super(message);
    }
}
