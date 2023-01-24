package com.lukdva.meetings.exceptions.forbidden;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import lombok.Getter;

@Getter
public class NonAdminCannotChangeUserRoleException extends ForbiddenException implements BaseExceptionInterface {
    private final Long userId;
    private final long errorCode = 7L;

    public NonAdminCannotChangeUserRoleException(Long userId) {
        super(String.format("User:%d unauthorized to change user role", userId));
        this.userId = userId;
    }
}
