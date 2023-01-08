package com.lukdva.meetings.exceptions.badRequest;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import com.lukdva.meetings.models.Meeting;
import com.lukdva.meetings.models.User;
import lombok.Getter;

@Getter
public class PersonAlreadyAddedToMeetingException extends BadRequestException implements BaseExceptionInterface {
    private final User person;
    private final Meeting meeting;
    private final long errorCode = 3L;

    public PersonAlreadyAddedToMeetingException(User person, Meeting meeting) {
        super(String.format("PersonId: %s , Meeting that the person already added Id: %s", person.getId(), meeting.getId()));
        this.person = person;
        this.meeting = meeting;
    }
}
