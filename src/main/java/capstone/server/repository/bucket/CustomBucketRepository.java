package capstone.server.repository.bucket;


import capstone.server.domain.bucket.Bucket;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomBucketRepository {

    List<Optional<Bucket>> searchBucket(BucketSearch bucketSearch);
}
