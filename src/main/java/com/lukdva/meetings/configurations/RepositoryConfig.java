package com.lukdva.meetings.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {

    private final ConfigProperties config;

    @Bean("MeetingsFile")
    public File meetingFile() {
        return new File(config.getRepositoryPath());
    }
}
