package my.gatheringservice.gathering.controller;

import lombok.RequiredArgsConstructor;
import my.gatheringservice.dto.request.AddGatheringRequest;
import my.gatheringservice.dto.request.UpdateGatheringRequest;
import my.gatheringservice.dto.response.*;
import my.gatheringservice.dto.response.gathering.AddGatheringResponse;
import my.gatheringservice.dto.response.gathering.GatheringPagingResponse;
import my.gatheringservice.dto.response.gathering.GatheringResponse;
import my.gatheringservice.dto.response.gathering.UpdateGatheringResponse;
import my.gatheringservice.gathering.service.GatheringService;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping(value = "/gathering",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<AddGatheringResponse> addGathering(@RequestPart AddGatheringRequest addGatheringRequest,
                                                             @RequestPart("file") MultipartFile file,
                                                             @RequestHeader("Authorization") String token){

            AddGatheringResponse addGatheringResponse = gatheringService.addGathering(addGatheringRequest,file,token);

            if(addGatheringResponse.getCode().equals("SU")) {
                return new ResponseEntity<>(addGatheringResponse, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(addGatheringResponse, HttpStatus.BAD_REQUEST);
            }


    }

    @PutMapping(value = "/gathering/{gatheringId}",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<UpdateGatheringResponse> updateGathering(@RequestPart UpdateGatheringRequest updateGatheringRequest,
                                                                   @PathVariable Long gatheringId,
                                                                   @RequestPart("file") MultipartFile file,
                                                                   @RequestHeader("Authorization") String token){
        UpdateGatheringResponse updateGatheringResponse = gatheringService.updateGathering(updateGatheringRequest,file,token,gatheringId);
        if(updateGatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(updateGatheringResponse,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(updateGatheringResponse,HttpStatus.BAD_REQUEST);

        }

    }

    @GetMapping("/gathering/{gatheringId}")
    public ResponseEntity<GatheringResponse> gatheringDetail(@PathVariable Long gatheringId,
                                                             @RequestHeader("Authorization") String token){


        GatheringResponse gatheringResponse = gatheringService.gatheringDetail(gatheringId,token);
        if(gatheringResponse.getCode().equals("SU")){
            return new ResponseEntity<>(gatheringResponse,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(gatheringResponse,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/gatherings")
    public ResponseEntity<GatheringPagingResponse> gatherings(@RequestParam int pageNum,
                                                                          @RequestParam String title,
                                                                          @RequestHeader("Authorization") String token) {

        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatherings(pageNum, token, title);
        if (gatheringPagingResponse.getCode().equals("SU")) {
            return new ResponseEntity<>(gatheringPagingResponse,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(gatheringPagingResponse,HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/gatherings/like")
    public ResponseEntity<GatheringPagingResponse> gatheringsLike(@RequestParam int pageNum,
                                                 @RequestHeader("Authorization") String token){

        GatheringPagingResponse gatheringPagingResponse = gatheringService.gatheringsLike(pageNum,token);
            if(gatheringPagingResponse.getCode().equals("SU")){
                return new ResponseEntity<>(gatheringPagingResponse,HttpStatus.OK);

            }else {
                return new ResponseEntity<>(gatheringPagingResponse,HttpStatus.BAD_REQUEST);
            }


    }


}
