package com.lukdva.meetings.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserCreateDto {
    @NotBlank
    @Schema(example = "blue_banana")
    private final String username;
    @NotBlank
    @Schema(example = "joe.doe@gmail.com")
    private final String email;
    @NotBlank
    @Schema(example = "S3CretWords!!")
    private final String password;
    @Schema(example = "Joe Doe")
    private String name;
}
