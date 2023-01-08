package com.lukdva.meetings.exceptions;

import org.springframework.http.HttpStatus;

public interface BaseExceptionInterface {
    HttpStatus getHttpStatus();

    long getErrorCode();
}
