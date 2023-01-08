package com.lukdva.meetings.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ApiError {
    private String errorMessage;

    private long errorCode;

    private Date timeStamp;

    public ApiError() {
        timeStamp = new Date();
    }
}
