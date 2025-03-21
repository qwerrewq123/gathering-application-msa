package gathering.msa.api_gateway.repository;

import dto.response.gathering.RecommendGatheringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Repository
@RequiredArgsConstructor
public class JwtRepository {
    private final StringRedisTemplate redisTemplate;
    private static final String KEY_FORMAT = "jwt::list::%s";

    public void createOrUpdate(Long clientId,String jwt) {
        redisTemplate.opsForValue().set(generateKey(clientId), jwt, Duration.ofMinutes(30));
    }

    public String read(String jwt) {
        Set<String> keys = scanKeys("jwt::list::*");
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        List<String> allValues = new ArrayList<>();
        for (String key : keys) {
            List<String> values = listOps.range(key, 0, -1);  // 리스트 전체 값 가져오기
            allValues.addAll(values);
        }
        String findJwt = allValues.stream()
                .filter(value -> value.equals(jwt))
                .findAny()
                .orElse(null);
        return findJwt;
    }

    private String generateKey(Long clientId) {
        return KEY_FORMAT.formatted(clientId);
    }

    private Set<String> scanKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection()
                .scan(ScanOptions.scanOptions().match(pattern).count(100).build())) {
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keys;
    }
}
