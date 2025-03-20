package gathering.msa.meeting.service;

import dto.response.attend.AddAttendResponse;
import dto.response.attend.DisAttendResponse;
import dto.response.attend.PermitAttendResponse;
import dto.response.user.UserResponse;
import exception.attend.AlreadyAttendExeption;
import exception.attend.NotFoundAttend;
import exception.attend.NotWithdrawException;
import exception.meeting.NotAuthorizeException;
import exception.meeting.NotFoundMeetingExeption;
import exception.user.NotFoundUserException;
import gathering.msa.meeting.client.UserServiceClient;
import gathering.msa.meeting.entity.Attend;
import gathering.msa.meeting.entity.Meeting;
import gathering.msa.meeting.entity.MeetingCount;
import gathering.msa.meeting.repository.AttendRepository;
import gathering.msa.meeting.repository.MeetingCountRepository;
import gathering.msa.meeting.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowflake.Snowflake;

import java.time.LocalDateTime;

import static util.ConstClass.*;


@Service
@Transactional
@RequiredArgsConstructor
public class AttendService {

        private final AttendRepository attendRepository;
        private final MeetingRepository meetingRepository;
        private final UserServiceClient userServiceClient;
        private final MeetingCountRepository meetingCountRepository;
        private final Snowflake snowflake = new Snowflake();
        public AddAttendResponse addAttend(Long meetingId, String username,Long gatheringId) {

            UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
            if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            Attend checkAttend = attendRepository.findByUserIdAndMeetingId(userResponse.getId(),meetingId);
            if(checkAttend != null) throw new AlreadyAttendExeption("Meeting already attend");
            Attend attend = Attend.of(snowflake,false,meeting,userResponse,LocalDateTime.now());
            attendRepository.save(attend);
            return AddAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        public DisAttendResponse disAttend(Long meetingId, Long attendId, String username,Long gatheringId) {

            UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
            if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            Attend attend = attendRepository.findByIdAndAccepted(attendId,true).orElseThrow(() -> new NotFoundAttend("Not Found Attend!!"));
            MeetingCount meetingCount = meetingCountRepository.findByMeetingId(meetingId).orElseThrow();
            Long createdById = meeting.getUserId();
            Long userId = userResponse.getId();
            checkMeetingOpener(createdById, userId, meeting, attend,meetingCount);
            return DisAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        public PermitAttendResponse permitAttend(Long meetingId, Long attendId, String username,Long gatheringId) {
            UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
            if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(()->new NotFoundMeetingExeption("no exist Meeting!!"));
            Attend attend = attendRepository.findById(attendId).orElseThrow(() -> new NotFoundAttend("no exist Attend!!"));
            MeetingCount meetingCount = meetingCountRepository.findByMeetingId(meetingId).orElseThrow();
            Long createdBy = meeting.getUserId();
            Long userId = userResponse.getId();
            if(!createdBy.equals(userId)) throw new NotAuthorizeException("this user has no permission");
            if(attend.getAccepted()) throw new AlreadyAttendExeption("already attend!!");
            meetingCount.setCount(meetingCount.getCount()+1);
            attend.changeAccepted(true);
            return PermitAttendResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
        }

        private void checkMeetingOpener(Long createdById, Long userId, Meeting meeting, Attend attend,MeetingCount meetingCount) {
            if(createdById.equals(userId)){
                if(meetingCount.getCount()>1){
                    throw new NotWithdrawException("Opener can disAttend Meeting when count = 1");
                }else{
                    attendRepository.delete(attend);
                    meetingRepository.delete(meeting);
                    meetingCountRepository.delete(meetingCount);
                }
            }else{
                meetingCount.changeCount(meetingCount.getCount()-1);
                attendRepository.delete(attend);
            }
        }
}
