package gathering.msa.meeting.controller;

import dto.response.attend.AddAttendResponse;
import dto.response.attend.DisAttendResponse;
import dto.response.attend.PermitAttendResponse;
import gathering.msa.meeting.annotation.Username;
import gathering.msa.meeting.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttendController {

    private final AttendService attendService;

    @PostMapping("/{gatheringId}/meeting/{meetingId}/attend")
    public ResponseEntity<AddAttendResponse> addAttend(@PathVariable Long meetingId,
                                                       @PathVariable Long gatheringId,
                                                       @Username String username
                                            ){

        AddAttendResponse addAttendResponse = attendService.addAttend(meetingId, username,gatheringId);
        return new ResponseEntity<>(addAttendResponse, HttpStatus.OK);
    }


    @PostMapping("/{gatheringId}/meeting/{meetingId}/disAttend/{attendId}")
    public ResponseEntity<DisAttendResponse> disAttend(@PathVariable Long meetingId,
                                                       @PathVariable Long gatheringId,
                                                       @PathVariable Long attendId,
                                                       @Username String username){

        DisAttendResponse disAttendResponse = attendService.disAttend(meetingId, attendId, username,gatheringId);
        return new ResponseEntity<>(disAttendResponse, HttpStatus.OK);
    }

    @PostMapping("/{gatheringId}/meeting/{meetingId}/permitAttend/{attendId}")
    public ResponseEntity<PermitAttendResponse> permitAttend(@PathVariable Long meetingId,
                                                             @PathVariable Long gatheringId,
                                                             @PathVariable Long attendId,
                                                             @Username String username){
        PermitAttendResponse permitAttendResponse = attendService.permitAttend(meetingId, attendId, username,gatheringId);
        return new ResponseEntity<>(permitAttendResponse, HttpStatus.OK);
    }
}
