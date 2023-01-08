package com.lukdva.meetings.services;

import com.lukdva.meetings.models.User;
import com.lukdva.meetings.models.UserDetailsImpl;
import com.lukdva.meetings.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyDatabaseUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {
        User exampleUser = new User();
        exampleUser.setUsername(username);
        Example<User> example = Example.of(exampleUser);
        Optional<User> actual = userRepository.findOne(example);
        User user = actual.orElseThrow(() -> new RuntimeException("Invalid credentials"));
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        return new UserDetailsImpl(user.getUsername(), user.getPasswordHash(), List.of(authority), user.getId());
    }
}
