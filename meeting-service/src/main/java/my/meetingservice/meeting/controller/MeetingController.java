package my.meetingservice.meeting.controller;

import lombok.RequiredArgsConstructor;
import my.meetingservice.dto.request.AddMeetingRequest;
import my.meetingservice.dto.request.UpdateMeetingRequest;
import my.meetingservice.dto.response.meeting.*;
import my.meetingservice.meeting.service.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/meeting/{gatheringId}")
    public ResponseEntity<AddMeetingResponse> addMeeting(@RequestBody AddMeetingRequest addMeetingRequest,
                                                         @PathVariable Long gatheringId,
                                                         @RequestHeader("Authorization") String token){


        AddMeetingResponse addMeetingResponse = meetingService.addMeeting(addMeetingRequest,token,gatheringId);
        if(addMeetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(addMeetingResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(addMeetingResponse, HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/meeting/{meetingId}")
    public ResponseEntity<Object> deleteMeeting(@RequestHeader("Authorization") String token,
                                                @PathVariable Long meetingId){


        DeleteMeetingResponse deleteMeetingResponse = meetingService.deleteMeeting(token,meetingId);
        if(deleteMeetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(deleteMeetingResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(deleteMeetingResponse, HttpStatus.BAD_REQUEST);
        }

    }


    @PatchMapping("/updateMeeting/{meetingId}")
    public ResponseEntity<Object> updateMeeting(@RequestBody UpdateMeetingRequest updateMeetingRequest,
                                                @RequestHeader("Authorization") String token,
                                                @PathVariable Long meetingId){


        UpdateMeetingResponse updateMeetingResponse = meetingService.updateMeeting(updateMeetingRequest,token,meetingId);
        if(updateMeetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(updateMeetingResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(updateMeetingResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<Object> meetingDetail(@PathVariable Long meetingId,
                                                @RequestHeader("Authorization") String token){


        MeetingResponse meetingResponse = meetingService.meetingDetail(meetingId,token);
        if(meetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(meetingResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(meetingResponse, HttpStatus.BAD_REQUEST);
        }


    }


    @GetMapping("/meetings")
    public ResponseEntity<Object> meetings(@RequestParam int pageNum,
                                           @RequestParam String title,
                                           @RequestHeader("Authorization") String token){

        MeetingPagingResponse pageMeetingResponse = meetingService.meetings(pageNum,token,title);

        if(pageMeetingResponse.getCode().equals("SU")){
            return new ResponseEntity<>(pageMeetingResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(pageMeetingResponse, HttpStatus.BAD_REQUEST);
        }

    }
}
