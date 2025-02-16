package my.gatheringservice.gathering.repository;

import my.gatheringservice.gathering.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GatheringRepository extends JpaRepository<Gathering,Long> {

    @Query("select g from Gathering g left join fetch GatheringCount  gc where g.id = :gatheringId")
    Optional<Gathering> fetchGathering(Long gatheringId);
    @Query("select g from Gathering g left join fetch GatheringCount  gc where g.title like %:title%")
    Page<Gathering> fetchPage(PageRequest pageRequest, String title);
}
