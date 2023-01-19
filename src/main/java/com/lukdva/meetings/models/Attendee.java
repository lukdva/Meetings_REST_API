package com.lukdva.meetings.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Attendee {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"meetingsOwned", "meetingsAttending", "attendees"})
    private User user;
    @ManyToOne
    @JoinColumn(name = "meeting_id")
    @JsonIgnoreProperties({"responsiblePerson", "attendees"})
    private Meeting meeting;
    private LocalDateTime added;

    public Attendee(User user, Meeting meeting){
        this.user= user;
        this.meeting = meeting;
        added= LocalDateTime.now();
    }
}
