//package com.lukdva.meetings.models;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class MeetingUnitTest {
//
//    @Test
//    void doesContainPersonAsAttendee() {
//        LocalDateTime date = LocalDateTime.now();
//        String name = "Test team meeting";
//        String description = "This is a meeting of test team";
//        Category category = Category.Hub;
//        Type type = Type.Live;
//        Person responsiblePerson = new Person("id1", "John Jones");
//        Person attendee = new Person("id2", "John Smith");
//
//        Meeting meeting = new Meeting(name, responsiblePerson, description, category, type, date, date);
//        meeting.addAttendee(attendee);
//        assertTrue(meeting.doesContainPersonAsAttendee(attendee.getId()));
//    }
//
//    @Test
//    void doesNotContainPersonAsAttendee() {
//        LocalDateTime date = LocalDateTime.now();
//        String name = "Test team meeting";
//        String description = "This is a meeting of test team";
//        Category category = Category.Hub;
//        Type type = Type.Live;
//        Person responsiblePerson = new Person("id1", "John Jones");
//        Person attendee = new Person("id2", "John Smith");
//
//        Meeting meeting = new Meeting(name, responsiblePerson, description, category, type, date, date);
//        meeting.addAttendee(attendee);
//        assertFalse(meeting.doesContainPersonAsAttendee(responsiblePerson.getId()));
//    }
//
//    @Test
//    void addAttendee() {
//        LocalDateTime date = LocalDateTime.now();
//        String name = "Test team meeting";
//        String description = "This is a meeting of test team";
//        Category category = Category.Hub;
//        Type type = Type.Live;
//        Person person = new Person("id1", "John Jones");
//        Person attendee = new Person("id2", "John Smith");
//        Meeting meeting = new Meeting(name, person, description, category, type, date, date);
//        meeting.addAttendee(attendee);
//
//        List<Attendee> attendees = meeting.getAttendees();
//        assertEquals(attendees.size(), 1);
//        Attendee createdAttendee = attendees.get(0);
//        assertNotEquals(createdAttendee.getId(), null);
//        assertEquals(createdAttendee.getPerson(), attendee);
//        assertTrue(createdAttendee.getAdded().isAfter(date) || createdAttendee.getAdded().equals(date));
//    }
//
//    @Test
//    void removeAttendee() {
//        LocalDateTime date = LocalDateTime.now();
//        String name = "Test team meeting";
//        String description = "This is a meeting of test team";
//        Category category = Category.Hub;
//        Type type = Type.Live;
//        Person responsiblePerson = new Person("id1", "John Jones");
//        Person attendee = new Person("id2", "John Smith");
//
//        Meeting meeting = new Meeting(name, responsiblePerson, description, category, type, date, date);
//        meeting.addAttendee(attendee);
//        List<Attendee> attendees = meeting.getAttendees();
//        assertEquals(attendees.size(), 1);
//        Attendee createdAttendee = attendees.get(0);
//        meeting.removeAttendee(createdAttendee);
//        assertEquals(attendees.size(), 0);
//    }
//
//    @Test
//    void isAttendeeAvailable() {
//        LocalDateTime date = LocalDateTime.now();
//        String name = "Test team meeting";
//        String description = "This is a meeting of test team";
//        Category category = Category.Hub;
//        Type type = Type.Live;
//        Person responsiblePerson = new Person("id1", "John Jones");
//        Person attendee = new Person("id2", "John Smith");
//
//        Meeting meeting = new Meeting(name, responsiblePerson, description, category, type, date, date);
//        meeting.addAttendee(attendee);
//        Attendee createdAttendee = meeting.getAttendees().get(0);
//        String attendeeId = createdAttendee.getId();
//        meeting.removeAttendee(createdAttendee);
//        assertFalse(meeting.isAttendeeAvailable(attendeeId));
//    }
//}