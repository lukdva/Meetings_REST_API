//package com.lukdva.meetings.utils;
//
//import com.lukdva.meetings.models.*;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MeetingBuilder {
//    private String id;
//    private String name = "Test team meeting";
//    private Person responsiblePerson = new Person(TestUtils.getRandomUUID(), "John Jones");
//    private String description = "This is a meeting of test team";
//    private Category category = Category.Hub;
//    private Type type = Type.Live;
//    private LocalDateTime startDate = LocalDateTime.of(2025, 10, 17, 10, 0); //2025-10-17 10:00:00;
//    private LocalDateTime endDate = LocalDateTime.of(2025, 10, 17, 11, 0); //2025-10-17 11:00:00;
//    private List<Person> attendees = new ArrayList<>();
//
//    public static MeetingBuilder aMeeting() {
//        return new MeetingBuilder();
//    }
//
//    public MeetingBuilder withId(String id) {
//        this.id = id;
//        return this;
//    }
//
//    public MeetingBuilder withName(String name) {
//        this.name = name;
//        return this;
//    }
//
//    public MeetingBuilder withResponsiblePerson(Person responsiblePerson) {
//        this.responsiblePerson = responsiblePerson;
//        return this;
//    }
//
//    public MeetingBuilder withDescription(String description) {
//        this.description = description;
//        return this;
//    }
//
//    public MeetingBuilder withCategory(Category category) {
//        this.category = category;
//        return this;
//    }
//
//    public MeetingBuilder withType(Type type) {
//        this.type = type;
//        return this;
//    }
//
//    public MeetingBuilder withStartDate(LocalDateTime startDate) {
//        this.startDate = startDate;
//        return this;
//    }
//
//    public MeetingBuilder withEndDate(LocalDateTime endDate) {
//        this.endDate = endDate;
//        return this;
//    }
//
//    public MeetingBuilder withAttendee(Person person) {
//        this.attendees.add(person);
//        return this;
//    }
//
//    public Meeting build() {
//        Meeting meeting = new Meeting(name, responsiblePerson, description, category, type, startDate, endDate);
//        if (id != null) {
//            meeting.setId(id);
//        }
//        attendees.forEach(meeting::addAttendee);
//        return meeting;
//    }
//}
