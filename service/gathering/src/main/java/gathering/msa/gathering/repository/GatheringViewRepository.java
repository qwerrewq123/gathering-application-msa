package gathering.msa.gathering.repository;

import gathering.msa.gathering.entity.GatheringView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import snowflake.Snowflake;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class GatheringViewRepository {
    private final StringRedisTemplate redisTemplate;
    private final BackUpGatheringViewRepository backUpGatheringViewRepository;
    private static final String KEY_FORMAT = "gathering::view::%s::gathering-count";
    private final Snowflake snowflake = new Snowflake();
    public Integer fetchCount(Long gatheringId, Duration ttl) {
        String key = generateKey(gatheringId);
        if(redisTemplate.opsForValue().get(key) == null){
            Long count = backUpGatheringViewRepository.findCountByGatheringId(gatheringId);
            if(count == null){
                redisTemplate.opsForValue().set(key, "1", ttl);
                backupSave(gatheringId,1);
                return 1;
            }
            redisTemplate.opsForValue().set(key, String.valueOf(count+1), ttl);
            return Integer.parseInt(redisTemplate.opsForValue().get(key));

        }else{
            Long count = redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key,ttl);
            return count.intValue();
        }
    }

    synchronized void backupSave(Long gatheringId,int count){
        backUpGatheringViewRepository.save(GatheringView.builder()
                .id(snowflake.nextId())
                .count(count)
                .gatheringId(gatheringId)
                .build());
    }

    synchronized void backupUpdate(Long gatheringId,int count){
        backUpGatheringViewRepository.updateCount(count,gatheringId);
    }

    private String generateKey(Long gatheringId) {
        return KEY_FORMAT.formatted(gatheringId);
    }
}
