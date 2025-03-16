package gathering.msa.gathering.repository;

import gathering.msa.gathering.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.userId = :userId")
    Enrollment existEnrollment(Long gatheringId,Long userId);


    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.userId = :userId and e.accepted = true")
    Optional<Enrollment> findEnrollment(Long gatheringId, Long userId);



}
