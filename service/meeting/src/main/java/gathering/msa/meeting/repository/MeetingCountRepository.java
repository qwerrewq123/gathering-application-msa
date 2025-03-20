package gathering.msa.meeting.repository;

import gathering.msa.meeting.entity.MeetingCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MeetingCountRepository extends JpaRepository<MeetingCount, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select mc from MeetingCount mc where mc.meeting.id = :meetingId")
    Optional<MeetingCount> findByMeetingId(Long meetingId);
}
