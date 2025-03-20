package gathering.msa.recommend.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class GatheringMeetingCreateRepository {
    private final StringRedisTemplate redisTemplate;
    private static final String KEY_FORMAT = "recommend::gathering::%s::meeting-state";

    public void createOrUpdate(Long gatheringId, Duration ttl) {
        String key = generateKey(gatheringId);
        if(redisTemplate.opsForValue().get(key) == null){
            redisTemplate.opsForValue().set(key, "1", ttl);
        }else{
            redisTemplate.opsForValue().increment(key, 1);
        }
    }

    public Long read(Long gatheringId) {
        String result = redisTemplate.opsForValue().get(generateKey(gatheringId));
        return result == null ? 0L : Long.valueOf(result);
    }

    private String generateKey(Long gatheringId) {
        return KEY_FORMAT.formatted(gatheringId);
    }
}
