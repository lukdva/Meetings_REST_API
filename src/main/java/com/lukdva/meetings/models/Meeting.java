package com.lukdva.meetings.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "responsible_person_id")
    @JsonIgnoreProperties({"meetingsOwned", "meetingsAttending", "attendees"})
    private User responsiblePerson;
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @OneToMany(mappedBy = "meeting", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnoreProperties({"meetingsOwned", "meetingsAttending", "attendees"})
    private List<Attendee> attendees = new ArrayList<>();

    public void addAttendee(Attendee attendee) {
        attendees.add(attendee);
    }

    public boolean doesContainPersonAsAttendee(Long id) {
        for (Attendee attendee : attendees) {
            if (attendee.getUser().getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Attendee getAttendee(Long id) {
        for (Attendee attendee : attendees) {
            if (attendee.getId().equals(id)) {
                return attendee;
            }
        }
        return null;
    }
}
