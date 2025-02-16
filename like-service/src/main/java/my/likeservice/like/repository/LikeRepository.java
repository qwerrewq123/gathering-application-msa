package my.likeservice.like.repository;

import my.likeservice.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {
    Optional<Like> fetchLike(Long id, Long gatheringId);
}
