package com.lukdva.meetings.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class ConfigProperties {
    private String jwtSecret;
    private String repositoryPath;
}
