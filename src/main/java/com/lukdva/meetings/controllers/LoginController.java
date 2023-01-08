package com.lukdva.meetings.controllers;


import com.lukdva.meetings.dtos.LoginDto;
import com.lukdva.meetings.models.UserDetailsImpl;
import com.lukdva.meetings.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Login")
public class LoginController {

    private final JwtEncoder encoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody LoginDto credentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        JwtClaimsSet claims = JwtUtils.generateJwtClaims(user);

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}