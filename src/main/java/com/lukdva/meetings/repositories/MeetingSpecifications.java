package com.lukdva.meetings.repositories;

import com.lukdva.meetings.models.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class MeetingSpecifications {
    static public Specification<Meeting> dateRangesOverlap(Meeting meetingToAttend){
        return (meeting, query, cb) -> cb.and(cb.lessThan(meeting.get("startDate"), meetingToAttend.getEndDate()), cb.greaterThan(meeting.get("endDate"), meetingToAttend.getStartDate()));
    }
    public static Specification<Meeting> meetingsThatUserIsAttending(Long userId){
        return (meeting, query, cb) -> {
            Join<Attendee, Meeting> meetingAttendees = meeting.join("attendees");
            query.groupBy(meeting.get("id"));
            return cb.equal(meetingAttendees.get("user").get("id"), userId);
        };
    }
    public static Specification<Meeting> startDateIsAfter(LocalDateTime start) {
        return (meeting, query, cb) -> cb.greaterThan(meeting.get("startDate"), start);
    }

    public static Specification<Meeting> endDateIsBefore(LocalDateTime end) {
        return (meeting, query, cb) -> cb.lessThan(meeting.get("endDate"), end);
    }

    public static Specification<Meeting> containsDescription(String description) {
        return (meeting, query, cb) -> cb.like(meeting.get("description"), "%" + description + "%");
    }

    public static Specification<Meeting> hasCategory(Category category) {
        return (meeting, query, cb) -> cb.equal(meeting.get("category"), category);
    }

    public static Specification<Meeting> hasType(Type type) {
        return (meeting, query, cb) -> cb.equal(meeting.get("type"), type);
    }

    public static Specification<Meeting> responsiblePersonHasId(Long id) {
        return (meeting, query, cb) -> {
            Join<User, Meeting> userMeetings = meeting.join("responsiblePerson");
            return cb.equal(userMeetings.get("id"), id);
        };
    }

    public static Specification<Meeting> hasMoreOrEqualAttendees(Integer count) {
        return (meeting, query, cb) -> cb.greaterThanOrEqualTo(cb.size(meeting.get("attendees")), count);
    }
}
