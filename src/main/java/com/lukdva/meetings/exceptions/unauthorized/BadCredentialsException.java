package com.lukdva.meetings.exceptions.unauthorized;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadCredentialsException extends UnauthorizedException implements BaseExceptionInterface {
    private final String username;
    private final long errorCode = 8L;
}
