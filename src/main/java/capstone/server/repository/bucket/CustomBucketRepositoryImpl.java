package capstone.server.repository.bucket;

import capstone.server.domain.bucket.Bucket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class CustomBucketRepositoryImpl implements CustomBucketRepository{


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Optional<Bucket>> searchBucket(BucketSearch bucketSearch) {
        return null;
    }
}
