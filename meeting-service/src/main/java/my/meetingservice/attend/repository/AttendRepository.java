package my.meetingservice.attend.repository;

import my.meetingservice.attend.Attend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendRepository extends JpaRepository<Attend,Long> {
}
