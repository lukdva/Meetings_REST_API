package com.lukdva.meetings.dtos;

import com.lukdva.meetings.models.Category;
import com.lukdva.meetings.models.Type;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeetingDTO {
    private String name;
    private PersonDTO responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public MeetingDTO() {
    }
}
