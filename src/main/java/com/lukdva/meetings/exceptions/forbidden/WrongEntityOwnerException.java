package com.lukdva.meetings.exceptions.forbidden;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import lombok.Getter;

@Getter
public class WrongEntityOwnerException extends ForbiddenException implements BaseExceptionInterface {
    private final String entityName;
    private final Long entityId;
    private final Long userId;
    private final long errorCode = 6L;

    public WrongEntityOwnerException(String entityName, Long entityId, Long userId) {
        super(String.format("User: %d, entityName: %s , entityId: %d", userId, entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
        this.userId = userId;
    }
}
