package my.alarmservice.alarm.controller;

import lombok.RequiredArgsConstructor;
import my.alarmservice.alarm.service.AlarmService;
import my.alarmservice.dto.request.AddAlarmRequest;
import my.alarmservice.dto.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PatchMapping("/alarm/{alarmId}")
    public ResponseEntity<CheckAlarmResponse> checkAlarm(@PathVariable Long alarmId, @RequestHeader("Authorization") String token){

        CheckAlarmResponse checkAlarmResponse = alarmService.checkAlarm(alarmId,token);

        if(checkAlarmResponse.getCode().equals("SU")){
            return new ResponseEntity<>(checkAlarmResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(checkAlarmResponse,HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/alarm/{alarmId}")
    public ResponseEntity<Object> deleteAlarm(@PathVariable Long alarmId,@RequestHeader("Authorization") String token){
        DeleteAlarmResponse deleteAlarmResponse = alarmService.deleteAlarm(alarmId,token);

        if(deleteAlarmResponse.getCode().equals("SU")){
            return new ResponseEntity<>(deleteAlarmResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(deleteAlarmResponse,HttpStatus.BAD_REQUEST);
        }



    }

    @GetMapping("/alarm")
    public ResponseEntity<PageAlarmResponse> alarmList(@RequestParam int page,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestParam Boolean checked){
        PageAlarmResponse pageAlarmResponse = alarmService.alarmList(page, token,checked);

        if(pageAlarmResponse.getCode().equals("SU")){
            return new ResponseEntity<>(pageAlarmResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(pageAlarmResponse,HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/alarm")
    public ResponseEntity<Object> addAlarm(@RequestBody AddAlarmRequest addAlarmRequest, @RequestHeader("Authorization") String token){
        AddAlarmResponse addAlarmResponse = alarmService.addAlarm(addAlarmRequest,token);

        if(addAlarmResponse.getCode().equals("SU")){
            return new ResponseEntity<>(addAlarmResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(addAlarmResponse,HttpStatus.BAD_REQUEST);
        }


    }
}
