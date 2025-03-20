package gathering.msa.meeting.repository;

import dto.response.meeting.MeetingDetailQuery;
import dto.response.meeting.MeetingsQuery;
import gathering.msa.meeting.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    @Query("select new dto.response.meeting." +
            "MeetingDetailQuery(m.id,m.title,m.userId,a.userId,m.boardDate,m.startDate,m.endDate,m.content,mc.count,m.imageId) " +
            "from Meeting m left join m.attends a " +
            "left join MeetingCount mc on mc.meeting.id = m.id " +
            "where m.id = :meetingId and a.accepted = true ")
    List<MeetingDetailQuery> meetingDetail(Long meetingId);
    @Query("select new dto.response.meeting." +
            "MeetingsQuery(m.id,m.title,m.userId,m.boardDate,m.startDate,m.endDate,m.content,mc.count,m.imageId) " +
            "from Meeting m " +
            "left join MeetingCount mc on mc.meeting.id = m.id " +
            "where m.title like %:title%")
    Page<MeetingsQuery> meetings(PageRequest pageRequest, String title);
}
