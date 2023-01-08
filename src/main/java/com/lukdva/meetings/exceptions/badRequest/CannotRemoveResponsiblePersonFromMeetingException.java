package com.lukdva.meetings.exceptions.badRequest;

import com.lukdva.meetings.exceptions.BaseExceptionInterface;
import com.lukdva.meetings.models.Meeting;
import com.lukdva.meetings.models.User;
import lombok.Getter;

@Getter
public class CannotRemoveResponsiblePersonFromMeetingException extends BadRequestException implements BaseExceptionInterface {

    private final long errorCode = 2L;
    private final User responsiblePerson;
    private final Meeting meeting;

    public CannotRemoveResponsiblePersonFromMeetingException(User responsiblePerson, Meeting meeting) {
        super(String.format("ResponsiblePersonId: %s , meetingId: %s", responsiblePerson.getId(), meeting.getId()));
        this.responsiblePerson = responsiblePerson;
        this.meeting = meeting;
    }
}
