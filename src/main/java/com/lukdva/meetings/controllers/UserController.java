package com.lukdva.meetings.controllers;

import com.lukdva.meetings.dtos.UserCreateDto;
import com.lukdva.meetings.dtos.UserEditDto;
import com.lukdva.meetings.dtos.UserResponseDto;
import com.lukdva.meetings.assemblers.UserMapper;
import com.lukdva.meetings.models.User;
import com.lukdva.meetings.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserCreateDto userCreateDto) {
        User savedUser = userService.createUser(userCreateDto);
        return UserMapper.toDto(savedUser);
    }

    @GetMapping
    @Operation(security = @SecurityRequirement(name = "bearer-key"))
    public List<UserResponseDto> getUsers() {
        return userService.getUsers().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @Operation(security = @SecurityRequirement(name = "bearer-key"))
    public UserResponseDto editUser(@RequestBody UserEditDto userEditDto, @PathVariable long id) {
        User user = userService.editUser(userEditDto, id);
        return UserMapper.toDto(user);
    }
}
