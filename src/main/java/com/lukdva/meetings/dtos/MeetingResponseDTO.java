package com.lukdva.meetings.dtos;

import com.lukdva.meetings.models.Attendee;
import com.lukdva.meetings.models.Category;
import com.lukdva.meetings.models.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MeetingResponseDTO {
    private Long id;
    private String name;
    private UserResponseDto responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Attendee> attendees = new ArrayList<>();

}
