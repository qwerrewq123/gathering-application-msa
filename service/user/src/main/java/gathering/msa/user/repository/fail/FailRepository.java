package gathering.msa.user.repository.fail;

import gathering.msa.user.entity.fail.Fail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailRepository extends JpaRepository<Fail, Long> {
}
