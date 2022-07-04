package capstone.server.repository.bucket;


import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketSearch;

import java.util.List;

public interface BucketRepositoryCustom {

    List<Bucket> searchBucket(BucketSearch bucketSearch);

}
