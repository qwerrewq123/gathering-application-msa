package my.userservice.user.repository;

import my.userservice.dto.response.UserResponse;
import my.userservice.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    @Query("select u from User u where u.id in :userIdList")
    List<User> fetchList(List<Long> userIdList);

    @Query("select u from User u where u.email = :email")
    List<User> findByEmail(String email);
}
