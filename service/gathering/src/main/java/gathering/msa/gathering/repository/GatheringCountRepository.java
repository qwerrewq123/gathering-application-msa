package gathering.msa.gathering.repository;

import gathering.msa.gathering.entity.GatheringCount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GatheringCountRepository extends JpaRepository<GatheringCount, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select gc from GatheringCount gc where gc.gathering.id = :gatheringId")
    Optional<GatheringCount> findByGathering(Long gatheringId);
}
