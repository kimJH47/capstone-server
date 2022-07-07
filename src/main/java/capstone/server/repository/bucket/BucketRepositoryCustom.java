package capstone.server.repository.bucket;


import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketSearch;

import java.util.List;
import java.util.Optional;

public interface BucketRepositoryCustom {

    List<Optional<Bucket>> searchBucket(BucketSearch bucketSearch);

}
