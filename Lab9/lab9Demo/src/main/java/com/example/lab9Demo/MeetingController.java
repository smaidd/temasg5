package com.example.lab9Demo;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MeetingController {
    private MeetingService service;

    public MeetingController(MeetingService service){
        this.service = service;
    }

    @GetMapping("/meetings")
    @ResponseBody
    public List<MeetingModel> getMeetings(@RequestParam(required = false)LocalDateTime start,
                                          @RequestParam(required = false) LocalDateTime end,
                                          @RequestParam(required = false) String roomId){
        return service.getMeetings(start, end, roomId);
    }

    @PutMapping("/meetings")
    public void scheduleMeeting (@RequestBody MeetingModel meeting){
        service.saveMeeting(meeting);
    }

    @DeleteMapping("/meetings/{id}")
    public void removeMeeting(@PathVariable String id){
        service.removeMeeting(id);
    }

    @GetMapping("/meetings/{meetingId}/attendees")
    public List<String> getAttendees(@PathVariable String meetingId){
        return service.getAttendees(meetingId);
    }
    @PutMapping("/meetings/{meetingId}/attendees")
    public void saveAttendees(@PathVariable String meetingId, @RequestBody List<String> attendees){
        service.saveAttendees(meetingId, attendees);
    }
}
