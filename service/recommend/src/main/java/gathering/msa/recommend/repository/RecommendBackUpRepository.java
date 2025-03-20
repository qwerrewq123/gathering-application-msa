package gathering.msa.recommend.repository;

import gathering.msa.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendBackUpRepository extends JpaRepository<Recommend,Long> {
}
