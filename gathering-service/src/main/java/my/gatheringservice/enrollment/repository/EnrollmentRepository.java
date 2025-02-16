package my.gatheringservice.enrollment.repository;

import my.gatheringservice.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.enrolledBy = :userId")
    Optional<Enrollment> fetchEnrollment(Long gatheringId, Long userId);
    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.enrolledBy = :userId and e.accepted = true")
    Optional<Enrollment> fetchAcceptedEnrollment(Long gatheringId, Long userId);
}
