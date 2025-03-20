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
public class MeetingCount {

    @Id
    private Long id;
    @Column(name = "gathering_id")
    private Long gatheringId;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    private Meeting meeting;
    private int count;
    public static MeetingCount of(Snowflake snowflake,Long gatheringId,Meeting meeting){
        return MeetingCount.builder()
                .gatheringId(gatheringId)
                .id(snowflake.nextId())
                .count(1)
                .meeting(meeting)
                .build();
    }

    public void changeCount(int count){
        this.count = count;
    }
}

