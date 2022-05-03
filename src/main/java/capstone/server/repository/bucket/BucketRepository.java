package capstone.server.repository.bucket;

import capstone.server.domain.bucket.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long> {

}

