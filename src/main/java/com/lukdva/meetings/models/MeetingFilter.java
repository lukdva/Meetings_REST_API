package com.lukdva.meetings.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MeetingFilter {
    private LocalDate start;
    private LocalDate end;
    private String description;
    private Long responsiblePersonId;
    private Category category;
    private Type type;
    private Integer attendees;

    public MeetingFilter() {
    }
}
