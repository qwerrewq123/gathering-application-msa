package gathering.msa.gathering.controller;

import dto.response.enrollment.DisEnrollGatheringResponse;
import dto.response.enrollment.EnrollGatheringResponse;
import gathering.msa.gathering.annotation.Username;
import gathering.msa.gathering.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    @PostMapping("/gathering/{gatheringId}/participate")
    public ResponseEntity<EnrollGatheringResponse> enrollGathering(@PathVariable Long gatheringId,
                                                                   @Username String username){

        EnrollGatheringResponse enrollGatheringResponse = enrollmentService.enrollGathering(gatheringId, username);
        return new ResponseEntity<>(enrollGatheringResponse, HttpStatus.OK);
    }

    @PostMapping("/gathering/{gatheringId}/disParticipate")
    public ResponseEntity<DisEnrollGatheringResponse> disEnrollGathering(@PathVariable Long gatheringId,
                                                                         @Username String username){

        DisEnrollGatheringResponse disEnrollGatheringResponse = enrollmentService.disEnrollGathering(gatheringId, username);
        return new ResponseEntity<>(disEnrollGatheringResponse, HttpStatus.OK);
    }
}
