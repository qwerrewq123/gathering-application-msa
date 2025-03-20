package gathering.msa.fcm.repository;

import gathering.msa.fcm.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    @Query("select t from FCMToken t where t.tokenValue =:tokenValue and t.userId =:userId")
    Optional<FCMToken> findByTokenAndUser(String tokenValue, Long userid);

    @Modifying
    @Query("delete from FCMToken t where t.tokenValue in :failedTokens")
    void deleteByTokenValueIn(@Param("failedTokens") List<String> failedTokens);

    @Query("select t from FCMToken t where t.expirationDate > :now")
    List<FCMToken> findByExpirationDate(LocalDate now);

    @Query("select t from FCMToken t where t.userId = :userId")
    List<FCMToken> findByUserId(Long userId);
}
