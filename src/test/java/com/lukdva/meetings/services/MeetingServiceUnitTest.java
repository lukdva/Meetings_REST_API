package com.lukdva.meetings.services;


import com.lukdva.meetings.exceptions.badRequest.CannotRemoveResponsiblePersonFromMeetingException;
import com.lukdva.meetings.exceptions.badRequest.PersonAlreadyAddedToMeetingException;
import com.lukdva.meetings.exceptions.badRequest.PersonHasConflictingMeetingException;
import com.lukdva.meetings.exceptions.notFound.NotFoundException;
import com.lukdva.meetings.exceptions.forbidden.WrongEntityOwnerException;
import com.lukdva.meetings.models.*;
import com.lukdva.meetings.repositories.AttendeesRepository;
import com.lukdva.meetings.repositories.MeetingRepository;
import com.lukdva.meetings.utils.JwtUtils;
import com.lukdva.meetings.utils.MeetingBuilder;
import com.lukdva.meetings.utils.UserBuilder;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceUnitTest {
    @Mock
    private MeetingRepository meetingRepository;
    @Mock
    private UserService userService;
    @Mock
    private AttendeesRepository attendeesRepository;
    @Mock
    private MockedStatic<JwtUtils> jwtUtils;
    @InjectMocks
    private MeetingsService meetingsService;
    @Captor
    private ArgumentCaptor<Meeting> meetingCaptor;
    @Captor
    private ArgumentCaptor<Attendee> attendeeCaptor;

    @Test
    void verifyAddMeetingCallsRepositoryWithUpdatedList() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().withResponsiblePerson(null).build();
        when(userService.getUser(any())).thenReturn(user);

        LocalDateTime before = LocalDateTime.now();
        meetingsService.addMeeting(meeting);
        LocalDateTime after = LocalDateTime.now();


        verify(meetingRepository).save(meetingCaptor.capture());
        boolean timeAttendeeWasAddedIsCorrect = meetingCaptor.getValue().getAttendees().get(0).getAdded().isAfter(before) && meetingCaptor.getValue().getAttendees().get(0).getAdded().isBefore(after);

        assertEquals(meetingCaptor.getValue().getResponsiblePerson(), user);
        assertEquals(meetingCaptor.getValue().getAttendees().get(0).getUser(), user);
        assertEquals(meetingCaptor.getValue().getAttendees().size(), 1);
        assertEquals(meetingCaptor.getValue().getAttendees().get(0).getMeeting(), meeting);
        assertTrue(timeAttendeeWasAddedIsCorrect);
    }

    @Test
    void getExistingMeeting() {
        Meeting meeting = MeetingBuilder.aMeeting().build();
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        Meeting retrievedMeeting = meetingsService.getMeeting(meeting.getId());
        assertEquals(retrievedMeeting, meeting);
    }

    @Test
    void tryingToGetNonExistentMeetingThrowsException() {
        when(meetingRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> meetingsService.getMeeting(1L));
        String expectedMessage = String.format("Not found entityName: %s , entityId: %d", "Meeting", 1L);
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void checkMeetingIsRemovedSuccessfully() {
        Meeting meeting = MeetingBuilder.aMeeting().build();
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));
        jwtUtils.when(JwtUtils::getUserId).thenReturn(meeting.getResponsiblePerson().getId());

        meetingsService.deleteMeeting(meeting.getId());
        verify(meetingRepository).deleteById(meeting.getId());
    }
    @Test
    void checkMeetingIsNotRemovedByNonResponsiblePerson() {
        Long userIdOfNonResponsiblePerson = 2L;
        Meeting meeting = MeetingBuilder.aMeeting().build();
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));
        jwtUtils.when(JwtUtils::getUserId).thenReturn(userIdOfNonResponsiblePerson);

        Exception exception = assertThrows(WrongEntityOwnerException.class, () -> meetingsService.deleteMeeting(meeting.getId()));
        String expectedMessage = String.format("User: %d, entityName: %s , entityId: %d", userIdOfNonResponsiblePerson, "Meeting", meeting.getId());
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void personHasConflictingMeetingsReturnsTrue() {
        Meeting meeting = MeetingBuilder.aMeeting().build();
        Meeting meetingUserAttends = MeetingBuilder.aMeeting().withUserAsAttendee(meeting.getResponsiblePerson()).build();

        when(meetingRepository.findAll((Specification<Meeting>) any())).thenReturn(List.of(meetingUserAttends));
        assertTrue(meetingsService.personHasConflictingMeetings(meeting.getResponsiblePerson(), meeting));
    }
    @Test
    void personHasConflictingMeetingsReturnsFalse() {
        Meeting meeting = MeetingBuilder.aMeeting().build();
        when(meetingRepository.findAll((Specification<Meeting>) any())).thenReturn(List.of());
        assertFalse(meetingsService.personHasConflictingMeetings(meeting.getResponsiblePerson(), meeting));
    }
    @Test
    void addPersonToMeetingSuccessfully() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().build();
        when(userService.getUser(user.getId())).thenReturn(user);
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        LocalDateTime before = LocalDateTime.now();
        Meeting updatedMeeting = meetingsService.addPersonToMeeting(meeting.getId(), user.getId());
        LocalDateTime after = LocalDateTime.now();

        verify(attendeesRepository).save(attendeeCaptor.capture());
        boolean timeAttendeeWasAddedIsCorrect = attendeeCaptor.getValue().getAdded().isAfter(before) && attendeeCaptor.getValue().getAdded().isBefore(after);

        assertEquals(attendeeCaptor.getValue().getUser(), user);
        assertEquals(attendeeCaptor.getValue().getMeeting(), updatedMeeting);
        assertTrue(timeAttendeeWasAddedIsCorrect);
    }

    @Test
    void tryingToAddAlreadyAddedPersonToMeetingThrowsException() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().withUserAsAttendee(user).build();
        when(userService.getUser(user.getId())).thenReturn(user);
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        Exception exception = assertThrows(PersonAlreadyAddedToMeetingException.class, () -> meetingsService.addPersonToMeeting(meeting.getId(), user.getId()));
        String expectedMessage = String.format("PersonId: %s , Meeting that the person already added Id: %s", user.getId(), meeting.getId());
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void tryingToAddPersonToConflictingMeetingThrowsException() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().build();
        Meeting meetingUserAttends = MeetingBuilder.aMeeting().withUserAsAttendee(user).build();
        when(userService.getUser(user.getId())).thenReturn(user);
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));
        when(meetingRepository.findAll((Specification<Meeting>) any())).thenReturn(List.of(meetingUserAttends));

        Exception exception = assertThrows(PersonHasConflictingMeetingException.class, () -> meetingsService.addPersonToMeeting(meeting.getId(), user.getId()));
        String expectedMessage = String.format("PersonId: %s , Meeting to be added to Id: %s", user.getId(), meeting.getId());
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void removeAttendeeFromMeetingSuccessfully() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().withId(8L).withUserAsAttendee(user).build();
        Attendee attendee = meeting.getAttendees().get(0);
        when(attendeesRepository.findById(attendee.getId())).thenReturn(Optional.of(attendee));
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        meetingsService.removeAttendeeFromMeeting(meeting.getId(), attendee.getId());

        verify(attendeesRepository).deleteById(attendee.getId());
    }
    @Test
    void tryingToRemoveNonExistingAttendeeThrowsException() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().withId(8L).withUserAsAttendee(user).build();
        Attendee attendee = meeting.getAttendees().get(0);
        Long nonExistingAttendeeId = 66L;

        when(attendeesRepository.findById(any())).thenReturn(Optional.empty());
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        Exception exception = assertThrows(NotFoundException.class, () -> meetingsService.removeAttendeeFromMeeting(meeting.getId(), nonExistingAttendeeId));
        String expectedMessage = String.format("Not found entityName: %s , entityId: %d", "Attendee", nonExistingAttendeeId);
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void tryingToRemoveNonExistingAttendeeAtTheMeetingThrowsException() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().withId(8L).withUserAsAttendee(user).build();
        Attendee attendee = meeting.getAttendees().get(0);
        Meeting meetingWithNoAttendees = MeetingBuilder.aMeeting().withId(9L).build();
        when(attendeesRepository.findById(attendee.getId())).thenReturn(Optional.of(attendee));
        when(meetingRepository.findById(meetingWithNoAttendees.getId())).thenReturn(Optional.of(meeting));

        Exception exception = assertThrows(RuntimeException.class, () -> meetingsService.removeAttendeeFromMeeting(meetingWithNoAttendees.getId(), attendee.getId()));
        String expectedMessage = String.format("Not found entityName: %s , entityId: %d", "Attendee", attendee.getId());
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
//
    @Test
    void tryingToRemoveResponsiblePersonAsAttendeeThrowsException() {
        User user = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().withId(8L).withResponsiblePerson(user).withUserAsAttendee(user).build();
        Attendee attendee = meeting.getAttendees().get(0);
        when(attendeesRepository.findById(attendee.getId())).thenReturn(Optional.of(attendee));
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));

        Exception exception = assertThrows(CannotRemoveResponsiblePersonFromMeetingException.class, () -> meetingsService.removeAttendeeFromMeeting(meeting.getId(), attendee.getId()));
        String expectedMessage = String.format("ResponsiblePersonId: %d , meetingId: %d", meeting.getResponsiblePerson().getId(), meeting.getId());
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deleteMeetingAsResponsiblePerson() {
        Meeting meeting = MeetingBuilder.aMeeting().build();
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));
        jwtUtils.when(JwtUtils::getUserId).thenReturn(meeting.getResponsiblePerson().getId());


        meetingsService.deleteMeeting(meeting.getId());
        Mockito.verify(meetingRepository).deleteById(meeting.getId());
    }

    @Test
    void tryingDeleteMeetingAsNonResponsiblePersonThrowsException() {
        User userNonResponsiblePerson = UserBuilder.anUser().build();
        Meeting meeting = MeetingBuilder.aMeeting().build();
        when(meetingRepository.findById(meeting.getId())).thenReturn(Optional.of(meeting));
        jwtUtils.when(JwtUtils::getUserId).thenReturn(userNonResponsiblePerson.getId());

        Exception exception = assertThrows(WrongEntityOwnerException.class, () -> meetingsService.deleteMeeting(meeting.getId()));
        String expectedMessage = String.format("User: %d, entityName: %s , entityId: %d", userNonResponsiblePerson.getId(), "Meeting", meeting.getId());
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Nested
    @DisplayName("Tests for filtering meetings")
//    @ExtendWith(MockitoExtension.class)
//    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestsFilteringMeetings {
        @Mock
        private Root<Meeting> root;
        @Mock
        private CriteriaQuery cq;
        @Mock
        private CriteriaBuilder cb;
        @Mock
        MeetingRepository meetingRepository;
        @Mock
        Path pathMock;
        @InjectMocks
        MeetingsService meetingsService;
        @Captor
        private ArgumentCaptor<Specification<Meeting>> specCaptor;

        @Test
        void filterByCategoryUsesCorrectSpecification() {
            MeetingFilter filters = new MeetingFilter();
            filters.setCategory(Category.TeamBuilding);
            when(root.get("category")).thenReturn(pathMock);

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);

            verify(cb, only()).equal(pathMock, Category.TeamBuilding);
        }
        @Test
        void filterByTypeReturnsCorrectSpecification() {
            MeetingFilter filters = new MeetingFilter();
            filters.setType(Type.Live);
            when(root.get("type")).thenReturn(pathMock);

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);

            verify(cb, only()).equal(pathMock, Type.Live);
        }
        @Test
        void filterByDescriptionReturnsCorrectSpecification() {
            MeetingFilter filters = new MeetingFilter();
            filters.setDescription("java");
            when(root.get("description")).thenReturn(pathMock);

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);

            verify(cb, only()).like(pathMock, "%java%");
        }
        @Test
        void filterByResponsiblePersonReturnsCorrectSpecification() {
            MeetingFilter filters = new MeetingFilter();
            filters.setResponsiblePersonId(1L);
            Join joinMock = Mockito.mock(Join.class);
            when(root.join("responsiblePerson")).thenReturn(joinMock);
            when(joinMock.get("id")).thenReturn(pathMock);

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);

            verify(cb, only()).equal(pathMock, 1L);
        }
        @Test
        void filterByStartDateReturnsCorrectSpecification() {
            LocalDate filterStart = LocalDate.of(2025, 10, 18); //2025-10-18
            MeetingFilter filters = new MeetingFilter();
            filters.setStart(filterStart);
            when(root.get("startDate")).thenReturn(pathMock);

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);

            verify(cb, only()).greaterThan(pathMock, filterStart.atStartOfDay());
        }

        @Test
        void filterByEndDateReturnsCorrectSpecification() {
            LocalDate filterStart = LocalDate.of(2025, 10, 18); //2025-10-18
            MeetingFilter filters = new MeetingFilter();
            filters.setEnd(filterStart);
            when(root.get("endDate")).thenReturn(pathMock);

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);

            verify(cb, only()).lessThan(pathMock, filterStart.plusDays(1).atStartOfDay());
        }

        @Test
        void filterByAttendeesCountReturnsCorrectSpecification() {
            MeetingFilter filters = new MeetingFilter();
            filters.setAttendees(2);
            Expression expressionMock = Mockito.mock(Expression.class);
            when(root.get("attendees")).thenReturn(pathMock);
            when(cb.size(pathMock)).thenReturn(expressionMock);

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);
            assertEquals(mockingDetails(cb).getInvocations().size(), 2);

            verify(cb,times(1)).greaterThanOrEqualTo(expressionMock, 2);
        }

        @Test
        void noFiltersAppliedReturnsCorrectSpecification() {
            MeetingFilter filters = new MeetingFilter();

            meetingsService.getFilteredMeetings(filters);
            verify(meetingRepository).findAll(specCaptor.capture());
            Specification<Meeting> specification = specCaptor.getValue();
            specification.toPredicate(root, cq, cb);

            assertEquals(mockingDetails(cb).getInvocations().size(), 0);
        }
    }
}