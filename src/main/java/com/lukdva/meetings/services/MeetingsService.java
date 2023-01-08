package com.lukdva.meetings.services;

import com.lukdva.meetings.models.*;
import com.lukdva.meetings.repositories.AttendeesRepository;
import com.lukdva.meetings.repositories.MeetingRepository;
import com.lukdva.meetings.utils.JwtUtils;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

        Attendee attendee = new Attendee(); //TODO create attendee service? and move this logic
        attendee.setUser(user);
        attendee.setMeeting(meeting);
        attendee.setAdded(LocalDateTime.now());
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

        Attendee attendee = new Attendee(); //TODO create attendee service? and move this logic
        attendee.setUser(user);
        attendee.setMeeting(meeting);
        attendee.setAdded(LocalDateTime.now());
        attendeesRepository.save(attendee);

        meeting.addAttendee(attendee);
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
            throw new RuntimeException("cannot remove responsible person");
        }
        attendeesRepository.deleteById(attendeeId);
    }

    public void deleteMeeting(Long meetingId, Long userId) {

        Meeting meeting = getMeeting(meetingId);
        if (!meeting.getResponsiblePerson().getId().equals(userId)) {//TODO maybe move userId extraction here (from controller)
//            throw new WrongEntityOwnerException("Meeting", meetingId, userId);// TODO refactor exception
            throw new RuntimeException("Unauthorized");
        }
        meetingRepository.deleteById(meetingId);
    }

    static Specification<Meeting> dateRangesOverlap(Meeting meetingToAttend){
        return (meeting, query, cb) -> cb.and(cb.lessThan(meeting.get("startDate"), meetingToAttend.getEndDate()), cb.greaterThan(meeting.get("endDate"), meetingToAttend.getStartDate()));
    }
    static Specification<Meeting> meetingsThatUserIsAttending(Long userId){
        return (meeting, query, cb) -> {
            Join<Attendee, Meeting> meetingAttendees = meeting.join("attendees");
            query.groupBy(meeting.get("id"));
            return cb.equal(meetingAttendees.get("user").get("id"), userId);
        };
    }
    static Specification<Meeting> startDateIsAfter(LocalDateTime start) {
        return (meeting, query, cb) -> cb.greaterThan(meeting.get("startDate"), start);
    }

    static Specification<Meeting> endDateIsBefore(LocalDateTime end) {
        return (meeting, query, cb) -> cb.greaterThan(meeting.get("endDate"), end);
    }

    static Specification<Meeting> containsDescription(String description) {
        return (meeting, query, cb) -> cb.like(meeting.get("description"), "%" + description + "%");
    }

    static Specification<Meeting> hasCategory(Category category) {
        return (meeting, query, cb) -> cb.equal(meeting.get("category"), category);
    }

    static Specification<Meeting> hasType(Type type) {
        return (meeting, query, cb) -> cb.equal(meeting.get("type"), type);
    }

    private Specification<Meeting> responsiblePersonHasId(Long id) {
        return (meeting, query, cb) -> {
            Join<User, Meeting> userMeetings = meeting.join("responsiblePerson");
            return cb.equal(userMeetings.get("id"), id);
        };
    }

    private Specification<Meeting> hasMoreOrEqualAttendees(Integer count) {
        return (meeting, query, cb) -> {
//            Join<Attendee, Meeting> meetingAttendees = meeting.join("attendees");
//            query.groupBy(meeting.get("id"));
//            query.having(cb.greaterThan(cb.count(meetingAttendees), count));
            return cb.greaterThanOrEqualTo(cb.size(meeting.get("attendees")), count);
        };
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
