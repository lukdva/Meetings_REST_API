package com.lukdva.meetings.utils;

import com.lukdva.meetings.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingBuilder {
    private Long id = 500L;
    private String name = "Test team meeting";
    private User responsiblePerson = UserBuilder.anUser().withId(200L).build(); //TODO consider null as default value
    private String description = "This is a meeting of test team";
    private Category category = Category.Hub;
    private Type type = Type.Live;
    private LocalDateTime startDate = LocalDateTime.of(2025, 10, 17, 10, 0); //2025-10-17 10:00:00;
    private LocalDateTime endDate = LocalDateTime.of(2025, 10, 17, 11, 0); //2025-10-17 11:00:00;
    private final List<User> usersAsAttendees = new ArrayList<>();

    public static MeetingBuilder aMeeting() {
        return new MeetingBuilder();
    }

    public MeetingBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MeetingBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MeetingBuilder withResponsiblePerson(User responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
        return this;
    }

    public MeetingBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MeetingBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public MeetingBuilder withType(Type type) {
        this.type = type;
        return this;
    }

    public MeetingBuilder withStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public MeetingBuilder withEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public MeetingBuilder withUserAsAttendee(User user) {
        this.usersAsAttendees.add(user);
        return this;
    }

    public Meeting build() {
        Meeting meeting = new Meeting(id ,name, responsiblePerson, description, category, type, startDate, endDate, new ArrayList<>());
        usersAsAttendees.forEach(user -> {
            Attendee attendee = new Attendee(user, meeting);
            meeting.addAttendee(attendee);
        });
        return meeting;
    }
}
