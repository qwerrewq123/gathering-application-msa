package gathering.msa.meeting.repository;

import gathering.msa.meeting.entity.Attend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttendRepository extends JpaRepository<Attend,Long> {
    @Query("select a from Attend  a where a.userId = :userId and a.meeting.id = :meetingId")
    Attend findByUserIdAndMeetingId(Long userId, Long meetingId);

    @Query("select a from Attend  a where a.userId = :userId and a.meeting.id = :meetingId and a.accepted = true")
    Attend findByUserIdAndMeetingIdAndTrue(Long userId,Long meetingId);
    @Query("select a from Attend a where a.accepted= :accepted")
    Optional<Attend> findByIdAndAccepted(Long attendId,boolean accepted);
}
