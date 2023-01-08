package com.lukdva.meetings.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    private String id;
    private String name;

    public PersonDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
