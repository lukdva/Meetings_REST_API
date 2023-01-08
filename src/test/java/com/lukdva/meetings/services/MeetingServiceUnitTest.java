//package com.lukdva.meetings.services;
//
//
//import com.lukdva.meetings.exceptions.badRequest.BadRequestException;
//import com.lukdva.meetings.exceptions.notFound.NotFoundException;
//import com.lukdva.meetings.exceptions.unauthorized.UnauthorizedException;
//import com.lukdva.meetings.models.*;
//import com.lukdva.meetings.repositories.MeetingRepository;
//import com.lukdva.meetings.utils.MeetingBuilder;
//import com.lukdva.meetings.utils.PersonBuilder;
//import com.lukdva.meetings.utils.TestUtils;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class MeetingServiceUnitTest {
//    @Mock
//    private MeetingRepository repository;
//    private MeetingsService meetingsService;
//    private final List<Meeting> meetings = new ArrayList<>();
//
//    @BeforeEach
//    public void setup() {
//        when(repository.readMeetings()).thenReturn(meetings);
//        meetingsService = new MeetingsService(repository);
//    }
//
//    @Test
//    void verifyAddMeetingCallsRepositoryWithUpdatedList() {
//        Meeting meeting = MeetingBuilder.aMeeting().build();
//        List<Meeting> expectedMeetingsList = List.of(meeting);
//        meetingsService.addMeeting(meeting);
//        verify(repository).writeMeetings(expectedMeetingsList);
//    }
//
//    @Test
//    void getExistingMeeting() {
//        Meeting meeting = MeetingBuilder.aMeeting().build();
//        meetings.add(meeting);
//
//        Meeting retrievedMeeting = meetingsService.getMeeting(meeting.getId());
//        assertEquals(retrievedMeeting, meeting);
//    }
//
//    @Test
//    void tryingToGetNonExistentMeetingThrowsException() {
//        Exception exception = assertThrows(NotFoundException.class, () -> meetingsService.getMeeting(TestUtils.getRandomUUID()));
//        String expectedMessage = "Meeting not found";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//    @Test
//    void checkMeetingIsRemovedSuccessfully() {
//        Meeting meeting = MeetingBuilder.aMeeting().build();
//        meetings.add(meeting);
//
//        meetingsService.removeMeeting(meeting);
//        Mockito.verify(repository).writeMeetings(new ArrayList<>());
//    }
//
//    @Test
//    void listOfMeetingsPersonIsInWhenPersonHasNoMeetings() {
//        List<Meeting> expectedEmptyList = List.of();
//        Person responsiblePerson = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withResponsiblePerson(responsiblePerson).withAttendee(responsiblePerson).build();
//        meetings.add(meeting);
//
//        Person personWithNoMeetings = PersonBuilder.aPerson().withId("id2").withName("John Smith").build();
//        List<Meeting> listOfMeetings = meetingsService.listOfMeetingsPersonIsIn(personWithNoMeetings);
//        assertIterableEquals(listOfMeetings, expectedEmptyList);
//    }
//
//    @Test
//    void listOfMeetingsPersonIsInWhenPersonAttendsOneOfTheMeetings() {
//
//        Person personParticipatingInMeeting = PersonBuilder.aPerson().withId("id2").withName("John Smith").build();
//        Meeting meetingWithoutAttendees = MeetingBuilder.aMeeting().build();
//        Meeting meetingPersonIsIn = MeetingBuilder.aMeeting().withAttendee(personParticipatingInMeeting).build();
//
//        List<Meeting> expectedMeetingList = List.of(meetingPersonIsIn);
//        meetings.add(meetingWithoutAttendees);
//        meetings.add(meetingPersonIsIn);
//
//        List<Meeting> listOfMeetings = meetingsService.listOfMeetingsPersonIsIn(personParticipatingInMeeting);
//        assertIterableEquals(listOfMeetings, expectedMeetingList);
//    }
//
//    @Test
//    void personHasConflictingMeetingsReturnsTrue() {
//        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 30, 0); //2025-10-17 10:30:00
//        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
//        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 10, 45, 0); //2025-10-17 10:45:00
//        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 11, 45, 0); //2025-10-17 11:45:00
//
//        Person personWithAConflictingMeeting = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withStartDate(start1).withEndDate(end1).withAttendee(personWithAConflictingMeeting).build();
//        Meeting meetingToBeAttended = MeetingBuilder.aMeeting().withStartDate(start2).withEndDate(end2).build();
//        meetings.add(meeting);
//        meetings.add(meetingToBeAttended);
//
//        assertTrue(meetingsService.personHasConflictingMeetings(personWithAConflictingMeeting, meetingToBeAttended));
//    }
//
//    @Test
//    void personHasConflictingMeetingsReturnsFalse() {
//        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 30, 0); //2025-10-17 10:30:00
//        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
//        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0); //2025-10-17 12:00:00
//        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 13, 0, 0); //2025-10-17 13:00:00
//
//        Person personWithoutConflictingMeeting = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withStartDate(start1).withEndDate(end1).withAttendee(personWithoutConflictingMeeting).build();
//        Meeting nonOverlappingMeeting = MeetingBuilder.aMeeting().withStartDate(start2).withEndDate(end2).build();
//        meetings.add(meeting);
//        meetings.add(nonOverlappingMeeting);
//
//        assertFalse(meetingsService.personHasConflictingMeetings(personWithoutConflictingMeeting, nonOverlappingMeeting));
//    }
//
//    @Test
//    void dateRangesOverlapReturnTrue() {
//        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0); //2025-10-17 10:00:00
//        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
//        Meeting meeting1 = MeetingBuilder.aMeeting().withStartDate(start1).withEndDate(end1).build();
//
//        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 10, 30, 0); //2025-10-17 10:30:00
//        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0); //2025-10-17 12:00:00
//        Meeting meeting2 = MeetingBuilder.aMeeting().withStartDate(start2).withEndDate(end2).build();
//
//        assertTrue(meetingsService.dateRangesOverlap(meeting1, meeting2));
//    }
//
//    @Test
//    void dateRangesOverlapReturnFalse() {
//        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0);  //2025-10-17 10:00:00
//        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0);  //2025-10-17 11:00:00
//        Meeting meeting1 = MeetingBuilder.aMeeting().withStartDate(start1).withEndDate(end1).build();
//
//        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 11, 30, 0);  //2025-10-17 11:30:00
//        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0);  //2025-10-17 12:00:00
//        Meeting meeting2 = MeetingBuilder.aMeeting().withStartDate(start2).withEndDate(end2).build();
//
//        assertFalse(meetingsService.dateRangesOverlap(meeting1, meeting2));
//    }
//
//    @Test
//    void dateRangesOverlapReturnFalseWhenBoundariesTouch() {
//        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0); //2025-10-17 10:00:00
//        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
//        Meeting meeting1 = MeetingBuilder.aMeeting().withStartDate(start1).withEndDate(end1).build();
//
//        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
//        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0); //2025-10-17 12:00:00
//        Meeting meeting2 = MeetingBuilder.aMeeting().withStartDate(start2).withEndDate(end2).build();
//
//        assertFalse(meetingsService.dateRangesOverlap(meeting1, meeting2));
//    }
//
//    @Test
//    void addPersonToMeetingSuccessfully() {
//        Person person = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().build();
//
//        meetingsService = TestUtils.getMeetingServiceWithInjectedStubbedRepository(repository, meeting);
//
//        Meeting updatedMeeting = meetingsService.addPersonToMeeting(meeting.getId(), person);
//        boolean doesMeetingContainPersonAsAttendee = updatedMeeting.getAttendees().stream().anyMatch(attendee -> attendee.getPerson().getId().equals(person.getId()));
//        assertTrue(doesMeetingContainPersonAsAttendee);
//        Mockito.verify(repository).writeMeetings(new ArrayList<>(List.of(updatedMeeting)));
//    }
//
//    @Test
//    void tryingToAddAlreadyAddedPersonToMeetingThrowsException() {
//        Person person = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withAttendee(person).build();
//        meetings.add(meeting);
//
//        Exception exception = assertThrows(BadRequestException.class, () -> meetingsService.addPersonToMeeting(meeting.getId(), person));
//        String expectedMessage = "Person already added to meeting";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//    @Test
//    void tryingToAddPersonToConflictingMeetingThrowsException() {
//        Person person = PersonBuilder.aPerson().build();
//        LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0); //2025-10-17 10:00:00
//        LocalDateTime end1 = LocalDateTime.of(2025, 10, 17, 11, 0, 0); //2025-10-17 11:00:00
//        Meeting meetingWithPersonAddedAsAttendee = MeetingBuilder.aMeeting().withStartDate(start1).withEndDate(end1).withAttendee(person).build();
//
//        LocalDateTime start2 = LocalDateTime.of(2025, 10, 17, 10, 30, 0); //2025-10-17 10:30:00
//        LocalDateTime end2 = LocalDateTime.of(2025, 10, 17, 12, 0, 0); //2025-10-17 12:00:00
//        Meeting overlappingMeeting = MeetingBuilder.aMeeting().withStartDate(start2).withEndDate(end2).build();
//
//        meetings.add(meetingWithPersonAddedAsAttendee);
//        meetings.add(overlappingMeeting);
//
//        Exception exception = assertThrows(BadRequestException.class, () -> meetingsService.addPersonToMeeting(overlappingMeeting.getId(), person));
//        String expectedMessage = "Person has conflicting meetings";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//    @Test
//    void removeAttendeeFromMeetingSuccessfully() {
//        Person person = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withAttendee(person).build();
//        String attendeeId = meeting.getAttendees().get(0).getId();
//        meetings.add(meeting);
//
//        Meeting updatedMeeting = meetingsService.removeAttendeeFromMeeting(meeting.getId(), attendeeId);
//        boolean isAttendeeRemovedFromMeeting = updatedMeeting.getAttendees().stream().noneMatch(attendee -> attendee.getPerson().getId().equals(person.getId()));
//        assertTrue(isAttendeeRemovedFromMeeting);
//        Mockito.verify(repository).writeMeetings(new ArrayList<>(List.of(updatedMeeting)));
//    }
//
//    @Test
//    void tryingToRemoveNonExistingAttendeeThrowsException() {
//        Person person = PersonBuilder.aPerson().build();
//        Meeting meetingWithAttendee = MeetingBuilder.aMeeting().withAttendee(person).build();
//        Meeting meetingWithOutAttendee = MeetingBuilder.aMeeting().build();
//        String attendeeId = meetingWithAttendee.getAttendees().get(0).getId();
//        meetings.add(meetingWithAttendee);
//        meetings.add(meetingWithOutAttendee);
//
//        Exception exception = assertThrows(NotFoundException.class, () -> meetingsService.removeAttendeeFromMeeting(meetingWithOutAttendee.getId(), attendeeId));
//        String expectedMessage = "Attendee not found";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//    @Test
//    void tryingToRemoveResponsiblePersonAsAttendeeThrowsException() {
//        Person person = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withResponsiblePerson(person).withAttendee(person).build();
//        String attendeeId = meeting.getAttendees().get(0).getId();
//        meetings.add(meeting);
//
//        Exception exception = assertThrows(BadRequestException.class, () -> meetingsService.removeAttendeeFromMeeting(meeting.getId(), attendeeId));
//        String expectedMessage = "Cannot remove responsible person from meeting";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//    @Test
//    void deleteMeetingAsResponsiblePerson() {
//        Person person = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withResponsiblePerson(person).build();
//        meetings.add(meeting);
//
//        meetingsService.deleteMeeting(meeting.getId(), person.getId());
//        Mockito.verify(repository).writeMeetings(new ArrayList<>(List.of()));
//    }
//
//    @Test
//    void tryingDeleteMeetingAsNonResponsiblePersonThrowsException() {
//        Person nonResponsiblePerson = PersonBuilder.aPerson().build();
//        Person responsiblePerson = PersonBuilder.aPerson().build();
//        Meeting meeting = MeetingBuilder.aMeeting().withResponsiblePerson(responsiblePerson).build();
//        meetings.add(meeting);
//
//        Exception exception = assertThrows(UnauthorizedException.class, () -> meetingsService.deleteMeeting(meeting.getId(), nonResponsiblePerson.getId()));
//        String expectedMessage = "Unauthorized";
//        String actualMessage = exception.getMessage();
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//    @Nested
//    @DisplayName("Tests for filtering meetings")
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
//    class TestsFilteringMeetings {
//        @Mock
//        MeetingRepository repository;
//        MeetingsService meetingsService;
//        Meeting meeting1;
//        Meeting meeting2;
//
//        @BeforeAll
//        public void createMeeting() {
//            LocalDateTime start1 = LocalDateTime.of(2025, 10, 17, 10, 0, 0); //2025-10-17 10:00:00
//            LocalDateTime end1 = LocalDateTime.of(2025, 10, 19, 11, 30, 0); //2025-10-19 11:30:00
//            meeting1 = MeetingBuilder.aMeeting()
//                    .withName("Test team meeting")
//                    .withDescription("This is a meeting of test team")
//                    .withCategory(Category.Hub)
//                    .withType(Type.Live)
//                    .withStartDate(start1)
//                    .withEndDate(end1)
//                    .withResponsiblePerson(PersonBuilder.aPerson().withId("ID1").build())
//                    .withAttendee(PersonBuilder.aPerson().build())
//                    .withAttendee(PersonBuilder.aPerson().build())
//                    .withAttendee(PersonBuilder.aPerson().build())
//                    .build();
//
//            LocalDateTime start2 = LocalDateTime.of(2025, 10, 18, 11, 30, 0); //2025-10-18 11:30:00
//            LocalDateTime end2 = LocalDateTime.of(2025, 10, 18, 12, 30, 0); //2025-10-18 12:30:00
//            meeting2 = MeetingBuilder.aMeeting()
//                    .withName("Team Java building")
//                    .withDescription("This is a meeting of building Java")
//                    .withCategory(Category.TeamBuilding)
//                    .withType(Type.InPerson)
//                    .withStartDate(start2)
//                    .withEndDate(end2)
//                    .build();
//
//            MockitoAnnotations.openMocks(this);
//            List<Meeting> meetingListToStub = new ArrayList<>(List.of(meeting1, meeting2));
//            meetingsService = TestUtils.getMeetingServiceWithInjectedStubbedRepository(repository, meetingListToStub);
//        }
//
//        @Test
//        void filterByCategoryReturnsMeeting() {
//            List<Meeting> expectedEmptyList = List.of(meeting2);
//            MeetingFilter filters = new MeetingFilter();
//            filters.setCategory("TeamBuilding");
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void filterByCategoryReturnsNoMeetings() {
//            List<Meeting> expectedEmptyList = List.of();
//            MeetingFilter filters = new MeetingFilter();
//            filters.setCategory("CodeMonkey");
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void filterByTypeReturnsMeeting() {
//            List<Meeting> expectedEmptyList = List.of(meeting1);
//            MeetingFilter filters = new MeetingFilter();
//            filters.setType("Live");
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void filterByDescriptionReturnsMeeting() {
//            List<Meeting> expectedEmptyList = List.of(meeting2);
//            MeetingFilter filters = new MeetingFilter();
//            filters.setDescription("java");
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void filterByResponsiblePersonReturnsMeeting() {
//            List<Meeting> expectedEmptyList = List.of(meeting1);
//            MeetingFilter filters = new MeetingFilter();
//            filters.setResponsiblePersonId("ID1");
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void filterByStartDateReturnsMeeting() {
//            List<Meeting> expectedEmptyList = List.of(meeting2);
//            LocalDate filterStart = LocalDate.of(2025, 10, 18); //2025-10-18
//            MeetingFilter filters = new MeetingFilter();
//            filters.setStart(filterStart);
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void filterByEndDateReturnsMeeting() {
//            List<Meeting> expectedEmptyList = List.of(meeting2);
//            LocalDate filterEnd = LocalDate.of(2025, 10, 18); //2025-10-18
//            MeetingFilter filters = new MeetingFilter();
//            filters.setEnd(filterEnd);
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void filterByAttendeesCountReturnsMeeting() {
//            List<Meeting> expectedEmptyList = List.of(meeting1);
//            MeetingFilter filters = new MeetingFilter();
//            filters.setAttendees(2);
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//        @Test
//        void noFiltersAppliedReturnsFullList() {
//            List<Meeting> expectedEmptyList = List.of(meeting1, meeting2);
//            MeetingFilter filters = new MeetingFilter();
//            List<Meeting> filteredMeetings = meetingsService.getFilteredMeetings(filters);
//            assertIterableEquals(filteredMeetings, expectedEmptyList);
//        }
//
//    }
//}