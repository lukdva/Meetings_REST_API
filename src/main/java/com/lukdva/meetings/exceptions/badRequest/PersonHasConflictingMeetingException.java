package com.lukdva.meetings.exceptions.badRequest;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import com.lukdva.meetings.models.Meeting;
import com.lukdva.meetings.models.User;
import lombok.Getter;

@Getter
public class PersonHasConflictingMeetingException extends BadRequestException implements BaseExceptionInterface {
    private final User user;
    private final Meeting meeting;
    private final long errorCode = 1L;


    public PersonHasConflictingMeetingException(User user, Meeting meeting) {
        super(String.format("UserId: %s , Meeting to be added to Id: %s", user.getId(), meeting.getId()));
        this.user = user;
        this.meeting = meeting;
    }

}
