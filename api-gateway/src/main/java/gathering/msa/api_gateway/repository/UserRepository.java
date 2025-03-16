package gathering.msa.api_gateway.repository;

import gathering.msa.api_gateway.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
