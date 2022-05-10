package capstone.server.repository.bucket;

import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;


@Transactional
@SpringBootTest
class BucketRepositoryTest {

    @Autowired
    private BucketRepository bucketRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    public void 버킷등록_테스트() throws Exception {
        //given
        bucketRepository.save(Bucket.builder()
                                    .content("버킷등록")
                                    .bucketStatus(BucketStatus.ONGOING)
                                    .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                    .uploadTime(LocalDateTime.now())
                                    .modifiedTime(LocalDateTime.now())
                                    .build());


        //when

        //then
    }
}