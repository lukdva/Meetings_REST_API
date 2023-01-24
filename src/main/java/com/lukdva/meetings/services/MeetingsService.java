package com.lukdva.meetings.services;

import com.lukdva.meetings.exceptions.badRequest.CannotRemoveResponsiblePersonFromMeetingException;
import com.lukdva.meetings.exceptions.badRequest.PersonAlreadyAddedToMeetingException;
import com.lukdva.meetings.exceptions.badRequest.PersonHasConflictingMeetingException;
import com.lukdva.meetings.exceptions.notFound.NotFoundException;
import com.lukdva.meetings.exceptions.forbidden.WrongEntityOwnerException;
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
              throw new PersonHasConflictingMeetingException(meeting.getResponsiblePerson(), meeting);
        }
        meetingRepository.save(meeting);
    }
    public Meeting getMeeting(Long id) {
        return meetingRepository.findById(id).orElseThrow(() -> new NotFoundException("Meeting", id));
    }
    public boolean personHasConflictingMeetings(User user, Meeting meetingToBeAttended) {
        List<Meeting> list = meetingRepository.findAll(where(meetingsThatUserIsAttending(user.getId())).and(dateRangesOverlap(meetingToBeAttended)));
        return list.size() > 0;

    }
    public Meeting addPersonToMeeting(Long meetingId, Long userId) {
        User user = userService.getUser(userId);
        Meeting meeting = getMeeting(meetingId);
        if (meeting.doesContainPersonAsAttendee(user.getId())) {
            throw new PersonAlreadyAddedToMeetingException(user, meeting);
        }
        if (personHasConflictingMeetings(user, meeting)) {
            throw new PersonHasConflictingMeetingException(user, meeting);
        }

        Attendee attendee = new Attendee(user, meeting);
        attendeesRepository.save(attendee);

        meeting.addAttendee(attendee);
        return meeting;
    }
    public void removeAttendeeFromMeeting(Long meetingId, Long attendeeId) {
        Meeting meeting = getMeeting(meetingId);
        Attendee attendee = attendeesRepository.findById(attendeeId).orElseThrow(() -> new NotFoundException("Attendee",attendeeId));

        if (!attendee.getMeeting().getId().equals(meetingId)) {
            throw new NotFoundException("Attendee", attendeeId);
        }
        if (attendee.getUser().getId().equals(meeting.getResponsiblePerson().getId())) {
            throw new CannotRemoveResponsiblePersonFromMeetingException(meeting.getResponsiblePerson(), meeting);
        }
        attendeesRepository.deleteById(attendeeId);
    }
    public void deleteMeeting(Long meetingId) {
        Long userId = JwtUtils.getUserId();
        Meeting meeting = getMeeting(meetingId);
        if (!meeting.getResponsiblePerson().getId().equals(userId)) {
            throw new WrongEntityOwnerException("Meeting", meetingId, userId);
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
