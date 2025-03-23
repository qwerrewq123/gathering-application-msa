package gathering.msa.gathering.service;

import dto.response.enrollment.DisEnrollGatheringResponse;
import dto.response.enrollment.EnrollGatheringResponse;
import dto.response.gathering.GatheringResponse;
import dto.response.user.UserResponse;
import event.EventType;
import event.payload.GatheringDisEnrollmentEventPayload;
import event.payload.GatheringEnrollmentEventPayload;
import exception.enrollment.AlreadyEnrollmentException;
import exception.enrollment.NotFoundEnrollmentException;
import exception.gathering.NotFoundGatheringException;
import exception.user.NotFoundUserException;
import gathering.msa.gathering.client.UserServiceClient;
import gathering.msa.gathering.entity.Enrollment;
import gathering.msa.gathering.entity.Gathering;
import gathering.msa.gathering.entity.GatheringCount;
import gathering.msa.gathering.repository.EnrollmentRepository;
import gathering.msa.gathering.repository.GatheringCountRepository;
import gathering.msa.gathering.repository.GatheringRepository;
import gathering.msa.outbox.OutboxEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snowflake.Snowflake;

import java.time.LocalDateTime;

import static util.ConstClass.*;


@Service
@Transactional
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final GatheringRepository gatheringRepository;
    private final UserServiceClient userServiceClient;
    private final GatheringCountRepository gatheringCountRepository;
    private final Snowflake snowflake = new Snowflake();
    private final OutboxEventPublisher outboxEventPublisher;

    public EnrollGatheringResponse enrollGathering(Long gatheringId, String username) {

        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                () -> new NotFoundGatheringException("no exist Gathering!!"));
        Enrollment exist = enrollmentRepository.existEnrollment(gatheringId, userResponse.getId());
        if(exist != null) throw new AlreadyEnrollmentException("Already enrolled;");
        Enrollment enrollment = Enrollment.of(snowflake,true,gathering,userResponse, LocalDateTime.now());
        enrollmentRepository.save(enrollment);
        GatheringCount gatheringCount = gatheringCountRepository.findByGathering(gatheringId).orElseThrow();
        gatheringCount.chagneCount(gatheringCount.getCount()+1);
        outboxEventPublisher.publish(EventType.GATHERING_ENROLLMENT,
                GatheringEnrollmentEventPayload.builder()
                        .id(enrollment.getId())
                        .accepted(enrollment.getAccepted())
                        .gatheringId(gatheringId)
                        .userId(enrollment.getUserId())
                        .date(enrollment.getDate())
                        .build(),
                gatheringId
                );
        return EnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public DisEnrollGatheringResponse disEnrollGathering(Long gatheringId, String username) {
        UserResponse userResponse = userServiceClient.fetchUserByUsername(username);
        if(!userResponse.getCode().equals(SUCCESS_CODE)) throw new NotFoundUserException("no exist User!!");
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(
                () -> new NotFoundGatheringException("no exist Gathering!!"));
        Enrollment enrollment = enrollmentRepository.findEnrollment(gatheringId, userResponse.getId()).orElseThrow(
                () ->  new NotFoundEnrollmentException("no exist Enrollment!!"));
        enrollmentRepository.delete(enrollment);
        GatheringCount gatheringCount = gatheringCountRepository.findByGathering(gatheringId).orElseThrow();
        gatheringCount.chagneCount(gatheringCount.getCount()-1);
        outboxEventPublisher.publish(EventType.GATHERING_DIS_ENROLLMENT,
                GatheringDisEnrollmentEventPayload.builder()
                        .id(enrollment.getId())
                        .accepted(enrollment.getAccepted())
                        .gatheringId(gatheringId)
                        .userId(enrollment.getUserId())
                        .date(enrollment.getDate())
                        .build(),
                gatheringId
        );
        return DisEnrollGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
}

