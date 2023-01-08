package com.lukdva.meetings.dtos;

import com.lukdva.meetings.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class UserResponseDto {
    @NotBlank
    private final long id;
    @NotBlank
    @Schema(example = "blue_banana")
    private final String username;
    @NotBlank
    @Schema(example = "joe.doe@gmail.com")
    private final String email;
    @Schema(example = "Joe Doe")
    private String name;
    @NotBlank
    private final Role role;
}
