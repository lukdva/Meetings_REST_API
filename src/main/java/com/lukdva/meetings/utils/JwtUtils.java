package com.lukdva.meetings.utils;

import com.lukdva.meetings.models.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Instant;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtUtils {
    private static final long expiry = 36000L;

    public static JwtClaimsSet generateJwtClaims(UserDetailsImpl user) {
        Instant now = Instant.now();
        String scope = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .claim("scope", scope)
                .build();
    }

    public static long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return (long) jwt.getClaims().get("userId");
    }

    public static Boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("SCOPE_ROLE_ADMIN"));
    }

}
