package my.meetingservice.attend.controller;

import lombok.RequiredArgsConstructor;
import my.meetingservice.attend.service.AttendService;
import my.meetingservice.dto.response.AddAttendResponse;
import my.meetingservice.dto.response.DisAttendResponse;
import my.meetingservice.dto.response.PermitAttendResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/meeting/{meetingId}/attend")
    public ResponseEntity<AddAttendResponse> addAttend(@PathVariable Long meetingId,
                                            @RequestHeader("Authorization") String token){


        AddAttendResponse addAttendResponse = attendService.addAttend(meetingId,token);
        if(addAttendResponse.getCode().equals("SU")){
            return new ResponseEntity<>(addAttendResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(addAttendResponse, HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/meeting/{meetingId}/disAttend/{attendId}")
    public ResponseEntity<DisAttendResponse> disAttend(@PathVariable Long meetingId,
                                            @PathVariable Long attendId,
                                            @RequestHeader("Authorization") String token){

        DisAttendResponse disAttendResponse = attendService.disAttend(meetingId,attendId,token);

        if(disAttendResponse.getCode().equals("SU")){
            return new ResponseEntity<>(disAttendResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(disAttendResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/meeting/{meetingId}/permitAttend/{attendId}")
    public ResponseEntity<Object> permitAttend(@PathVariable Long meetingId,
                                               @PathVariable Long attendId,
                                               @RequestHeader("Authorization") String token){

        PermitAttendResponse permitAttendResponse =  attendService.permitAttend(meetingId,attendId,token);
        if(permitAttendResponse.getCode().equals("SU")){
            return new ResponseEntity<>(permitAttendResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(permitAttendResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
