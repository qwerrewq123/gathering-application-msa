package gathering.msa.fcm.repository;

import gathering.msa.fcm.entity.Topic;
import gathering.msa.fcm.entity.UserTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {
    @Query("select ut from UserTopic ut where ut.userId =:userId")
    List<UserTopic> findByUser(Long userId);

    @Query("select ut from UserTopic ut join ut.topic t where t.topicName = :topicName and ut.userId = :userId")
    boolean existsByTopicAndUser(String topicName, Long userId);

    @Query(value = "delete from user_topic ut where ut.topic_id = :topicId and ut.user_id = :userId",nativeQuery = true)
    @Modifying
    void deleteByTopicAndUser(Long topicId, Long userId);
}
