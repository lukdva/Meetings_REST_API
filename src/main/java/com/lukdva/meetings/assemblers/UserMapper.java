package com.lukdva.meetings.assemblers;

import com.lukdva.meetings.dtos.UserResponseDto;
import com.lukdva.meetings.models.User;

public class UserMapper {

    public static UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        if (user.getName() != null) {
            userResponseDto.setName(user.getName());
        }
        return userResponseDto;
    }
}
