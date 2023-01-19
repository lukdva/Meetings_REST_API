package com.lukdva.meetings.utils;

import com.lukdva.meetings.models.Attendee;
import com.lukdva.meetings.models.Meeting;
import com.lukdva.meetings.models.Role;
import com.lukdva.meetings.models.User;

import java.util.HashSet;
import java.util.Set;

public class UserBuilder {
    private Long id = 100L;
    private String username = "user";
    private String email = "user@mail.com";
    private String passwordHash = "passwordHash";
    private String name = "Joe Doe";
    private Role role = Role.ROLE_USER;
    private final Set<Meeting> meetingsOwned = new HashSet<>();
    private final Set<Attendee> attendees = new HashSet<>();

    public static UserBuilder anUser(){
        return new UserBuilder();
    }
    public UserBuilder withId(Long id){
        this.id = id;
        return this;
    }
    public UserBuilder withUsername(String username){
        this.username = username;
        return this;
    }
    public UserBuilder withEmail(String email){
        this.email = email;
        return this;
    }
    public UserBuilder withPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
        return this;
    }
    public UserBuilder withName(String name){
        this.name = name;
        return this;
    }
    public UserBuilder withRole(Role role){
        this.role = role;
        return this;
    }
    public UserBuilder withMeeting(Meeting meeting){
        this.meetingsOwned.add(meeting);
        return this;
    }
    public UserBuilder withAttendee(Attendee attendee){
        this.attendees.add(attendee);
        return this;
    }
    public User build(){
        return new User(this.id, this.username, this.email, this.passwordHash, this.name, this.role, this.meetingsOwned, this.attendees);
    }
}
