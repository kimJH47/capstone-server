package capstone.server.repository.sns;
import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.reactions.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface HeartRepository extends JpaRepository<Heart,Long> {
    Optional<Heart> findByUserAndBucket(User user, Bucket bucket);
}
