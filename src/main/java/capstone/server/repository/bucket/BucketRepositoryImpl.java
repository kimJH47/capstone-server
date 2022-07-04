package capstone.server.repository.bucket;

import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketSearch;
import capstone.server.domain.bucket.BucketStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static capstone.server.domain.bucket.QBucket.bucket;


@RequiredArgsConstructor
public class BucketRepositoryImpl implements BucketRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Bucket> searchBucket(BucketSearch bucketSearch) {
        return jpaQueryFactory.selectFrom(bucket)
                              .where(bucket.bucketPrivacyStatus.eq(BucketPrivacyStatus.PUBLIC),eqContent(bucketSearch.getContent()),eqStatus(bucketSearch.getBucketStatus()))
                              .fetch();
    }

    private BooleanExpression eqContent(String content) {
        return content != null ? bucket.content.contains(content) : null;
    }
    private BooleanExpression eqStatus(BucketStatus bucketStatus) {
        return bucketStatus != null ? bucket.bucketStatus.eq(bucketStatus) : null;
    }

}
