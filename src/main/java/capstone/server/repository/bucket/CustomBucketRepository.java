package capstone.server.repository.bucket;


import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketSearch;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomBucketRepository {

    List<Optional<Bucket>> searchBucket(BucketSearch bucketSearch);
}
