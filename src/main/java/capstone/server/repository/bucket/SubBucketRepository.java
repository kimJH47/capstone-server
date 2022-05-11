package capstone.server.repository.bucket;

import capstone.server.domain.bucket.SubBucket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubBucketRepository extends JpaRepository<SubBucket, Long> {
    //Exception handling X
    List<SubBucket> findByBucketId(Long id);
}

