package com.lukdva.meetings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MeetingsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetingsApplication.class, args);
    }

}
