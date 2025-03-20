package gathering.msa.fcm.repository;

import gathering.msa.fcm.entity.FCMToken;
import gathering.msa.fcm.entity.FCMTokenTopic;
import gathering.msa.fcm.entity.Topic;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FCMTokenTopicRepository extends JpaRepository<FCMTokenTopic, Long> {
    void deleteByTopic(Topic topic);


    List<FCMTokenTopic> findByFcmTokenIn(List<FCMToken> tokens);

    @Transactional
    @Modifying
    @Query("DELETE FROM FCMTokenTopic tt WHERE tt.fcmToken.tokenValue IN :tokenValues")
    void deleteByTokenValueIn(@Param("tokenValues") List<String> tokenValues);
}
