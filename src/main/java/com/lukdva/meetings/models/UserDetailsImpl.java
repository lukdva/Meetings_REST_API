package com.lukdva.meetings.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsImpl extends User {
    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities, long id){
        super(username, password, authorities);
        this.id = id;
    }
    @Getter
    @Setter
    private long id;
}
