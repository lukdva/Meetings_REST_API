package com.lukdva.meetings.services;

import com.lukdva.meetings.models.Attendee;
import com.lukdva.meetings.models.Meeting;
import com.lukdva.meetings.models.MeetingFilter;
import com.lukdva.meetings.models.User;
import com.lukdva.meetings.repositories.AttendeesRepository;
import com.lukdva.meetings.repositories.MeetingRepository;
import com.lukdva.meetings.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lukdva.meetings.repositories.MeetingSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class MeetingsService {

    private final MeetingRepository meetingRepository;
    private final AttendeesRepository attendeesRepository;
    private final UserService userService;

    public void addMeeting(Meeting meeting) {
        User user = userService.getUser(JwtUtils.getUserId());
        meeting.setResponsiblePerson(user);

        Attendee attendee = new Attendee(user, meeting);
        meeting.addAttendee(attendee);

        if (personHasConflictingMeetings(meeting.getResponsiblePerson(), meeting)) {
            //  throw new PersonHasConflictingMeetingException(meeting.getResponsiblePerson(), meeting);//TODO refactor this exception class
            throw new RuntimeException("Person has conflicting meetings");
        }
        //TODO move responsible person's addition as attendee here (from MeetingAssembler)
        meetingRepository.save(meeting);
    }
    public Meeting getMeeting(Long id) {
//        return meetingRepository.findById(id).orElseThrow(() -> new NotFoundException("Meeting", id)); //TODO refactor this exception class
        return meetingRepository.findById(id).orElseThrow(() -> new RuntimeException("Meeting not found"));
    }
    public boolean personHasConflictingMeetings(User user, Meeting meetingToBeAttended) {
        List<Meeting> list = meetingRepository.findAll(where(meetingsThatUserIsAttending(user.getId())).and(dateRangesOverlap(meetingToBeAttended)));
        return list.size() > 0;

    }
    public Meeting addPersonToMeeting(Long meetingId, Long userId) {
        User user = userService.getUser(userId);
        Meeting meeting = getMeeting(meetingId);
        if (meeting.doesContainPersonAsAttendee(user.getId())) {
//            throw new PersonAlreadyAddedToMeetingException(person, meeting);// TODO refactor exception
            throw new RuntimeException("User already added to meeting");
        }
        if (personHasConflictingMeetings(user, meeting)) {
//            throw new PersonHasConflictingMeetingException(person, meeting);//TODO refactor exception
            throw new RuntimeException("Person has conflicting meeting");
        }

        Attendee attendee = new Attendee(user, meeting);
        attendeesRepository.save(attendee);

        meeting.addAttendee(attendee); //TODO Is this neccessary?
        return meeting;
    }
    public void removeAttendeeFromMeeting(Long meetingId, Long attendeeId) {
        Meeting meeting = getMeeting(meetingId);
        Attendee attendee = attendeesRepository.findById(attendeeId).orElseThrow(() -> new RuntimeException("Attendee not found")); //TODO move to attendee service and add specific exception

        if (!attendee.getMeeting().getId().equals(meetingId)) {
//            throw new NotFoundException("Attendee", attendeeId);//TODO refactor exception
            throw new RuntimeException("Attendee is not available at the meeting");
        }
        if (attendee.getUser().getId().equals(meeting.getResponsiblePerson().getId())) {
//            throw new CannotRemoveResponsiblePersonFromMeetingException(meeting.getResponsiblePerson(), meeting);// TODO refactor exception
            throw new RuntimeException("Cannot remove responsible person from meeting");
        }
        attendeesRepository.deleteById(attendeeId);
    }
    public void deleteMeeting(Long meetingId) {
        Long userId = JwtUtils.getUserId();
        Meeting meeting = getMeeting(meetingId);
        if (!meeting.getResponsiblePerson().getId().equals(userId)) {
//            throw new WrongEntityOwnerException("Meeting", meetingId, userId);// TODO refactor exception
            throw new RuntimeException("Unauthorized");
        }
        meetingRepository.deleteById(meetingId);
    }
    public List<Meeting> getFilteredMeetings(MeetingFilter filters) {
        Specification<Meeting> spec = where(null);

        if (filters.getDescription() != null) {
            spec = spec.and(containsDescription(filters.getDescription()));
        }
        if (filters.getResponsiblePersonId() != null) {
            spec = spec.and(responsiblePersonHasId(filters.getResponsiblePersonId()));
        }
        if (filters.getCategory() != null) {
            spec = spec.and(hasCategory(filters.getCategory()));
        }
        if (filters.getType() != null) {
            spec = spec.and(hasType(filters.getType()));
        }
        if (filters.getAttendees() != null) {
            spec = spec.and(hasMoreOrEqualAttendees(filters.getAttendees()));
        }
        if (filters.getStart() != null) {
            spec = spec.and(startDateIsAfter(filters.getStart().atStartOfDay()));
        }
        if (filters.getEnd() != null) {
            spec = spec.and(endDateIsBefore(filters.getEnd().plusDays(1).atStartOfDay()));
        }
        return meetingRepository.findAll(spec);
    }
}
