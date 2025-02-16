package my.gatheringservice.enrollment.controller;

import lombok.RequiredArgsConstructor;
import my.gatheringservice.dto.response.DisEnrollGatheringResponse;
import my.gatheringservice.dto.response.EnrollGatheringResponse;
import my.gatheringservice.enrollment.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/gathering/{gatheringId}/participate")
    public ResponseEntity<EnrollGatheringResponse> enrollGathering(@PathVariable Long gatheringId,
                                                  @Header("Authorization") String token){

        EnrollGatheringResponse enrollGatheringResponse = enrollmentService.enrollGathering(gatheringId,token);
        if(enrollGatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(enrollGatheringResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(enrollGatheringResponse,HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/gathering/{gatheringId}/disParticipate")
    public ResponseEntity<DisEnrollGatheringResponse> disEnrollGathering(@PathVariable Long gatheringId,
                                                     @Header("Authorization") String token){
        DisEnrollGatheringResponse disEnrollGatheringResponse = enrollmentService.disEnrollGathering(gatheringId,token);
        if(disEnrollGatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(disEnrollGatheringResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(disEnrollGatheringResponse,HttpStatus.BAD_REQUEST);

        }

    }
}
