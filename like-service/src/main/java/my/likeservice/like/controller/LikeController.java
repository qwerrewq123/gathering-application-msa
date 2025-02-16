package my.likeservice.like.controller;

import lombok.RequiredArgsConstructor;
import my.likeservice.dto.response.DislikeResponse;
import my.likeservice.dto.response.LikeResponse;
import my.likeservice.like.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PatchMapping("/like/{gatheringId}")
    public ResponseEntity<LikeResponse> like(@PathVariable Long gatheringId, @RequestHeader("Authorization") String token){

        LikeResponse likeResponse = likeService.like(gatheringId,token);
        if(likeResponse.getCode().equals("SU")){
            return new ResponseEntity<>(likeResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(likeResponse,HttpStatus.BAD_REQUEST);
        }
    }


    @PatchMapping("/dislike/{gatheringId}")
    public ResponseEntity<Object> dislike(@PathVariable Long gatheringId, @RequestHeader("Authorization") String token){
        DislikeResponse dislikeResponse = likeService.dislike(gatheringId,token);
        if(dislikeResponse.getCode().equals("SU")){
            return new ResponseEntity<>(dislikeResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(dislikeResponse,HttpStatus.BAD_REQUEST);
        }
    }
}
