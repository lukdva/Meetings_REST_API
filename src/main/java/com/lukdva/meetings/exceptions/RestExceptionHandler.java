package com.lukdva.meetings.exceptions;

import com.lukdva.meetings.exceptions.badRequest.CannotRemoveResponsiblePersonFromMeetingException;
import com.lukdva.meetings.exceptions.badRequest.PersonAlreadyAddedToMeetingException;
import com.lukdva.meetings.exceptions.badRequest.PersonHasConflictingMeetingException;
import com.lukdva.meetings.exceptions.notFound.NotFoundException;
import com.lukdva.meetings.exceptions.unauthorized.BadTokenException;
import com.lukdva.meetings.exceptions.unauthorized.WrongEntityOwnerException;
import com.lukdva.meetings.models.ApiError;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler {

    private final ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        Object[] objectsToFormatMessage = new Object[]{e.getEntityName(), e.getEntityId()};
        ApiError error = generateErrorResponse(objectsToFormatMessage, e.getErrorCode(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CannotRemoveResponsiblePersonFromMeetingException.class)
    public ResponseEntity<ApiError> handleCannotRemoveResponsiblePersonFromMeetingException(CannotRemoveResponsiblePersonFromMeetingException e) {
        Object[] objectsToFormatMessage = new Object[]{e.getResponsiblePerson().getName(), e.getMeeting().getId()};
        ApiError error = generateErrorResponse(objectsToFormatMessage, e.getErrorCode(), e);
        return new ResponseEntity<>(error, e.getHttpStatus());
    }

    @ExceptionHandler(value = PersonAlreadyAddedToMeetingException.class)
    public ResponseEntity<ApiError> handlePersonAlreadyAddedToMeetingException(PersonAlreadyAddedToMeetingException e) {
        Object[] objectsToFormatMessage = new Object[]{e.getPerson().getName(), e.getMeeting().getId()};
        ApiError error = generateErrorResponse(objectsToFormatMessage, e.getErrorCode(), e);
        return new ResponseEntity<>(error, e.getHttpStatus());
    }

    @ExceptionHandler(value = PersonHasConflictingMeetingException.class)
    public ResponseEntity<ApiError> handlePersonHasConflictingMeetingException(PersonHasConflictingMeetingException e) {
        Object[] objectsToFormatMessage = new Object[]{e.getPerson().getName(), e.getMeeting().getStartDate(), e.getMeeting().getEndDate()};
        ApiError error = generateErrorResponse(objectsToFormatMessage, e.getErrorCode(), e);
        return new ResponseEntity<>(error, e.getHttpStatus());
    }

    @ExceptionHandler(value = BadTokenException.class)
    public ResponseEntity<ApiError> handleBadTokenException(BadTokenException e) {
        Object[] objectsToFormatMessage = new Object[]{e.getToken()};
        ApiError error = generateErrorResponse(objectsToFormatMessage, e.getErrorCode(), e);
        return new ResponseEntity<>(error, e.getHttpStatus());
    }

    @ExceptionHandler(value = WrongEntityOwnerException.class)
    public ResponseEntity<ApiError> handleWrongEntityOwnerException(WrongEntityOwnerException e) {
        Object[] objectsToFormatMessage = new Object[]{e.getUserId(), e.getEntityName(), e.getEntityId()};
        ApiError error = generateErrorResponse(objectsToFormatMessage, e.getErrorCode(), e);
        return new ResponseEntity<>(error, e.getHttpStatus());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiError> handleDefaultException(RuntimeException e) {
        ApiError error = new ApiError();
        error.setErrorMessage(e.getMessage());
        log.warn(e);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ApiError generateErrorResponse(Object[] objectList, long errorCode, RuntimeException e) {

        String pattern = bundle.getString(Long.toString(errorCode));
        MessageFormat formatter = new MessageFormat(pattern);
        String formattedMessage = formatter.format(objectList);

        ApiError error = new ApiError();
        error.setErrorMessage(formattedMessage);
        error.setErrorCode(errorCode);

        log.warn(formattedMessage, e);

        return error;
    }
}
