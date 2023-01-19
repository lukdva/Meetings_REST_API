package com.lukdva.meetings.controllers;

import com.lukdva.meetings.assemblers.MeetingAssembler;
import com.lukdva.meetings.dtos.MeetingDTO;
import com.lukdva.meetings.dtos.MeetingResponseDTO;
import com.lukdva.meetings.models.MeetingFilter;
import com.lukdva.meetings.models.Meeting;
import com.lukdva.meetings.services.MeetingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.lukdva.meetings.assemblers.MeetingAssembler.toResponseDto;

@RestController
@RequestMapping("/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingsService meetingsService;

    @PostMapping
    public MeetingResponseDTO createMeeting(@RequestBody MeetingDTO meetingDto) {
        Meeting meeting = MeetingAssembler.mapMeeting(meetingDto);
        meetingsService.addMeeting(meeting);
        return toResponseDto(meeting);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeeting(@PathVariable Long id) {
        meetingsService.deleteMeeting(id);
    }

    @PostMapping("/{id}/attendees/{userId}")
    public MeetingResponseDTO addAttendee(@PathVariable Long id, @PathVariable Long userId) {
        Meeting updatedMeeting = meetingsService.addPersonToMeeting(id, userId);
        return toResponseDto(updatedMeeting);
    }

    @DeleteMapping("/{meetingId}/attendees/{attendeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAttendee(@PathVariable Long meetingId, @PathVariable Long attendeeId) {
        meetingsService.removeAttendeeFromMeeting(meetingId, attendeeId);
    }

    @GetMapping
    public List<MeetingResponseDTO> getMeetings(@RequestBody MeetingFilter filters) {
        List<Meeting> meetings = meetingsService.getFilteredMeetings(filters);
        return meetings.stream().map(MeetingAssembler::toResponseDto).collect(Collectors.toList());
    }
}
