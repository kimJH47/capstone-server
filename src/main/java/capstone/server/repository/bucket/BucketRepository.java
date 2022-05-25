package capstone.server.repository.bucket;

import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BucketRepository extends JpaRepository<Bucket, Long> {

    List<Bucket> findAllByUser(User user);

}

