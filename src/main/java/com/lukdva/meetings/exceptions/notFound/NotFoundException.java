package com.lukdva.meetings.exceptions.notFound;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException implements BaseExceptionInterface {
    private final String entityName;
    private final String entityId;
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    private final long errorCode = 4L;

    public NotFoundException(String entityName, String entityId) {
        super(String.format("Not found entityName: %s , entityId: %s", entityName, entityId));
        this.entityName = entityName;
        this.entityId = entityId;
    }

}
