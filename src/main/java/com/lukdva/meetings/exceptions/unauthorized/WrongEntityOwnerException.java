package com.lukdva.meetings.exceptions.unauthorized;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import lombok.Getter;

@Getter
public class WrongEntityOwnerException extends UnauthorizedException implements BaseExceptionInterface {
    private final String entityName;
    private final String entityId;
    private final String userId;
    private final long errorCode = 6L;

    public WrongEntityOwnerException(String entityName, String entityId, String userId) {
        super(String.format("User: %s, entityName: %s , entityId: %s", userId, entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
        this.userId = userId;
    }
}
