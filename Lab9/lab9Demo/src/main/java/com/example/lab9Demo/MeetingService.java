package com.example.lab9Demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingService {
    private MeetingRepository repository;

    public MeetingService (MeetingRepository repository){
        this.repository = repository;
    }

    public List<MeetingModel> getMeetings (LocalDateTime start, LocalDateTime end, String roomId){
        return repository.getMeetings(start, end, roomId);
    }
    @Transactional
    public void saveMeeting(MeetingModel meeting){
        repository.saveMeeting(meeting);
    }
    @Transactional
    public void removeMeeting(String id){
        repository.removeMeeting(id);
    }
    public List<String> getAttendees (String meetingId){
        return repository.getAttendees(meetingId);
    }
    @Transactional
    public void saveAttendees(String meetingId, List<String> attendees){
        final List<String> existingAttendees = repository.getAttendees(meetingId);
        attendees.stream().filter(attendee -> !existingAttendees.contains(attendee))
                .forEach(attendee -> repository.addAttendee(meetingId, attendee));
        existingAttendees.stream().filter(attendee -> !attendees.contains(attendee))
                .forEach(attendee -> repository.removeAttendee(meetingId, attendee));
    }
}
