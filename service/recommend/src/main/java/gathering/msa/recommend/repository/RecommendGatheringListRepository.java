package gathering.msa.recommend.repository;

import dto.response.gathering.GatheringPagingResponse;
import dto.response.gathering.RecommendGatheringResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import util.ConstClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static util.ConstClass.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecommendGatheringListRepository {
    private final StringRedisTemplate redisTemplate;
    private static final String KEY_FORMAT = "recommend::list::%s";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public void add(Long gatheringId, LocalDateTime time, Long score, Long limit, Duration ttl) {
        redisTemplate.executePipelined((RedisCallback<?>) action -> {
            StringRedisConnection conn = (StringRedisConnection) action;
            String key = generateKey(time);
            conn.zAdd(key, score, String.valueOf(gatheringId));
            conn.zRemRange(key, 0, - limit - 1);
            conn.expire(key, ttl.toSeconds());
            return null;
        });
    }
    public Set<ZSetOperations.TypedTuple<String>> findAll(LocalDateTime time) {
        String key = generateKey(time);
        return redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
    }


    public void remove(Long articleId, LocalDateTime time) {
        redisTemplate.opsForZSet().remove(generateKey(time), String.valueOf(articleId));
    }

    private String generateKey(LocalDateTime time) {
        return generateKey(TIME_FORMATTER.format(time));
    }

    private String generateKey(String dateStr) {
        return KEY_FORMAT.formatted(dateStr);
    }

    public RecommendGatheringResponse readAll(String dateStr) {
        List<Long> listIds = redisTemplate.opsForZSet()
                .reverseRangeWithScores(generateKey(dateStr), 0, -1).stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .map(Long::valueOf)
                .toList();
        return RecommendGatheringResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,null);
    }
}
