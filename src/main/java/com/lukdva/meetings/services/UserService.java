package com.lukdva.meetings.services;

import com.lukdva.meetings.dtos.UserCreateDto;
import com.lukdva.meetings.dtos.UserEditDto;
import com.lukdva.meetings.models.Role;
import com.lukdva.meetings.models.User;
import com.lukdva.meetings.repositories.UserRepository;
import com.lukdva.meetings.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(UserCreateDto userCreateDto) {
        String passwordHash = passwordEncoder.encode(userCreateDto.getPassword());
        User user = new User(userCreateDto.getUsername(), userCreateDto.getEmail(), passwordHash);
        user.setName(userCreateDto.getName());
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User editUser(UserEditDto userEditDto, long id) {
        Boolean isAdmin = JwtUtils.isAdmin();
        Boolean isSelfEditing = JwtUtils.getUserId() == id;

        if (!(isAdmin || isSelfEditing)) {
            throw new RuntimeException("Unauthorized");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (userEditDto.getUsername() != null) {
            user.setUsername(userEditDto.getUsername());
        }
        if (userEditDto.getEmail() != null) {
            user.setEmail(userEditDto.getEmail());
        }
        if (userEditDto.getPassword() != null) {
            String passwordHash = passwordEncoder.encode(userEditDto.getPassword());
            user.setPasswordHash(passwordHash);
        }
        if (userEditDto.getRole() != null) {
            if (!isAdmin) {
                throw new RuntimeException("Unauthorized to change user role");
            }
            user.setRole(userEditDto.getRole());
        }
        if (userEditDto.getName() != null) {
            user.setName(userEditDto.getName());
        }
        return userRepository.save(user);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));//TODO use specific exception class
    }
}
