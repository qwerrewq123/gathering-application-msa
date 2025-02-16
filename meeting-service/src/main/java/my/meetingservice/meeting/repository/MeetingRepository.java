package my.meetingservice.meeting.repository;

import my.meetingservice.meeting.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select m from Meeting  m where m.title like %:title%")
    Page<Meeting> fetchPageMeeting(PageRequest pageRequest, String title);

    @Query("select m from Meeting m left join fetch Attend  a where m.id = :meetingId")
    Optional<Meeting> fetchById(Long meetingId);
}
