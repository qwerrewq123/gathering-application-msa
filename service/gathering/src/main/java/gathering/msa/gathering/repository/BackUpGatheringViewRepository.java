package gathering.msa.gathering.repository;

import gathering.msa.gathering.entity.GatheringView;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BackUpGatheringViewRepository extends JpaRepository<GatheringView,Long> {
    @Query("select g.count from GatheringCount g where g.gathering.id = :gatheringId")
    Long findCountByGatheringId(@Param("gatheringId") Long gatheringId);

    @Query(value = "update gathering_view set count = :count" +
            " where count > :count and gathering_id = :gatheringId",nativeQuery = true)
    @Modifying
    void updateCount(@Param("count") Integer count, @Param("gatheringId") Long gatheringId);
}
