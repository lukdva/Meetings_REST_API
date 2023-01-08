package com.lukdva.meetings.dtos;

import com.lukdva.meetings.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@RequiredArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserEditDto {
    @Schema(example = "blue_banana")
    private String username;
    @Schema(example = "joe.doe@gmail.com")
    private String email;
    @Schema(example = "S3CretWords!!")
    private String password;
    @Schema(example = "Joe Doe")
    private String name;
    private Role role;
}