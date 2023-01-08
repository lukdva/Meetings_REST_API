package com.lukdva.meetings.assemblers;

import com.lukdva.meetings.dtos.MeetingDTO;
import com.lukdva.meetings.dtos.MeetingResponseDTO;
import com.lukdva.meetings.models.Meeting;

public class MeetingAssembler {
    public static Meeting mapMeeting(MeetingDTO meetingDto) {
        Meeting meeting = new Meeting();
        meeting.setName(meetingDto.getName());
        meeting.setDescription(meetingDto.getDescription());
        meeting.setCategory(meetingDto.getCategory());
        meeting.setType(meetingDto.getType());
        meeting.setStartDate(meetingDto.getStartDate());
        meeting.setEndDate(meetingDto.getEndDate());
        return meeting;
    }

    public static MeetingResponseDTO toResponseDto(Meeting meeting) {
        MeetingResponseDTO meetingResponseDTO = new MeetingResponseDTO();
        meetingResponseDTO.setId(meeting.getId());
        meetingResponseDTO.setName(meeting.getName());
        meetingResponseDTO.setResponsiblePerson(UserMapper.toDto(meeting.getResponsiblePerson()));
        meetingResponseDTO.setDescription(meeting.getDescription());
        meetingResponseDTO.setCategory(meeting.getCategory());
        meetingResponseDTO.setType(meeting.getType());
        meetingResponseDTO.setStartDate(meeting.getStartDate());
        meetingResponseDTO.setEndDate(meeting.getEndDate());
        meetingResponseDTO.setAttendees(meeting.getAttendees());//TODO create attendee mapper add only attendeeid userid and meetingid

        return meetingResponseDTO;
    }
}
