package capstone.server.repository.bucket;

import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
public class BucketRepositoryImpl implements BucketRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Optional<Bucket>> searchBucket(BucketSearch bucketSearch) {
        return null;
    }
}