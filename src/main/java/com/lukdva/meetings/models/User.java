package com.lukdva.meetings.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "responsiblePerson")
    @JsonIgnoreProperties({"responsiblePerson", "attendees"})
    private Set<Meeting> meetingsOwned;
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({"responsiblePerson", "attendees"})
    private Set<Attendee> attendees;

    public User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
