package com.lukdva.meetings.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank
    @Schema(example = "blue_banana")
    private String username;
    @NotBlank
    @Schema(example = "S3CretWords!!")
    private String password;
}
