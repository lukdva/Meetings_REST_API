package com.lukdva.meetings.utils;

import java.util.UUID;

public class TestUtils {

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

//    public static MeetingsService getMeetingServiceWithInjectedStubbedRepository(MeetingRepository repository, List<Meeting> meetings) {
//        when(repository.readMeetings()).thenReturn(meetings);
//        return new MeetingsService(repository);
//    }
//
//    public static MeetingsService getMeetingServiceWithInjectedStubbedRepository(MeetingRepository repository, Meeting meeting) {
//        List<Meeting> meetings = new ArrayList<>(List.of(meeting));
//        when(repository.readMeetings()).thenReturn(meetings);
//        return new MeetingsService(repository);
//    }
}
