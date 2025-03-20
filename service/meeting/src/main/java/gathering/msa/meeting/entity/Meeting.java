package gathering.msa.meeting.entity;

import dto.request.meeting.AddMeetingRequest;
import dto.response.gathering.GatheringResponse;
import dto.response.image.SaveImageResponse;
import dto.response.user.UserResponse;
import jakarta.persistence.*;
import lombok.*;
import snowflake.Snowflake;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Meeting {

    @Id
    private Long id;
    private String title;
    private LocalDateTime boardDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    @Column(name = "user_id")
    private Long userId;
    @OneToMany(mappedBy = "meeting")
    private List<Attend> attends = new ArrayList<>();
    @Column(name = "gathering_id")
    private Long gatheringId;
    @Column(name = "image_id")
    private Long imageId;

    public void attend(List<Attend> attends){
        for (Attend attend : attends) {
            attend.addMeeting(this);
        }
        this.attends = attends;
    }
    public static Meeting of(Snowflake snowflake,AddMeetingRequest addMeetingRequest, SaveImageResponse saveImageResponse, UserResponse userResponse, GatheringResponse gatheringResponse){
        return Meeting.builder()
                .id(snowflake.nextId())
                .title(addMeetingRequest.getTitle())
                .content(addMeetingRequest.getContent())
                .userId(userResponse.getId())
                .boardDate(LocalDateTime.now())
                .startDate(addMeetingRequest.getStartDate())
                .endDate(addMeetingRequest.getEndDate())
                .gatheringId(gatheringResponse.getId())
                .imageId(saveImageResponse.getIds().getFirst())
                .build();
    }
}

