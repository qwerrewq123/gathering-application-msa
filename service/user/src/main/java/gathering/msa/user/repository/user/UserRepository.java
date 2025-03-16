package gathering.msa.user.repository.user;

import gathering.msa.user.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByNickname(String nickname);

    @Query("select u from User u where u.email = :email")
    List<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.profileImage where u.id = :userId")
    Optional<User> findById(Long userId);
}
