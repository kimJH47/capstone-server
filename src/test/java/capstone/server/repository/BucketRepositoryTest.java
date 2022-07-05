package capstone.server.repository;

import capstone.server.domain.User;
import capstone.server.domain.bucket.*;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.bucket.SubBucketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(JpaConfig.class)
class BucketRepositoryTest {

    @Autowired
    private BucketRepository bucketRepository;
    @Autowired
    private SubBucketRepository subBucketRepository;
    @Autowired
    EntityManager entityManager;
    @Test
    @DisplayName("BucketSearch 를 이용해서 조회하면 해당 필드조건에 맞는 버킷List 가 반환되어야함(쿼리확인)")
    public void 버킷동적조회_컨탠츠() throws Exception{
        //given
        User user = getUser("1", "email@tesat.com", "testName");

        entityManager.persist(user);

        Bucket bucket = getBucket("버킷내용1", BucketPrivacyStatus.PUBLIC, user);
        SubBucket subBucket1 = getSubBucket("세부목표1");
        SubBucket subBucket2 = getSubBucket("세부목표2");
        bucket.addSubBucket(subBucket1);
        bucket.addSubBucket(subBucket2);
        bucketRepository.save(bucket);


        Bucket bucket2= getBucket("버킷내용2", BucketPrivacyStatus.PUBLIC, user);
        SubBucket subBucket3 = getSubBucket("세부목표1");
        SubBucket subBucket4 = getSubBucket("세부목표2");

        bucket2.addSubBucket(subBucket3);
        bucket2.addSubBucket(subBucket4);
        bucketRepository.save(bucket2);

        BucketSearch bucketSearch = new BucketSearch();
        bucketSearch.setContent("버킷");
        //when
        List<Bucket> buckets = bucketRepository.searchBucket(bucketSearch);
        //then
        assertEquals(true, buckets.stream()
                                  .map(Bucket::getContent)
                                  .allMatch(content -> content.matches("(.*)버킷(.*)")));
        assertEquals(buckets.size(),2);

    }
    @Test
    @DisplayName("BucketSearch 를 이용해서 조회하면 해당 필드조건에 맞는 버킷List 가 반환되어야함(쿼리확인)")
    public void 버킷동적조회_버킷상태() throws Exception{
        //given
        User user = getUser("1", "email@tesat.com", "testName");
        entityManager.persist(user);

        Bucket bucket = getBucket("버킷", BucketPrivacyStatus.PUBLIC, user);
        SubBucket subBucket1 = getSubBucket("세부목표1");
        SubBucket subBucket2 = getSubBucket("세부목표2");
        bucket.addSubBucket(subBucket1);
        bucket.addSubBucket(subBucket2);
        bucketRepository.save(bucket);

        Bucket bucket2= getBucket("TestContent", BucketPrivacyStatus.PUBLIC, user);
        SubBucket subBucket3 = getSubBucket("세부목표1");
        SubBucket subBucket4 = getSubBucket("세부목표2");

        bucket2.addSubBucket(subBucket3);
        bucket2.addSubBucket(subBucket4);
        bucketRepository.save(bucket2);

        BucketSearch bucketSearch = new BucketSearch();
        bucketSearch.setBucketStatus(BucketStatus.ONGOING);
        //when
        List<Bucket> buckets = bucketRepository.searchBucket(bucketSearch);
        //then

        assertEquals(true, buckets.stream()
                                   .map(Bucket::getBucketStatus)
                                   .allMatch(bucketStatus -> bucketStatus.equals(BucketStatus.ONGOING)));;
        assertEquals(buckets.size(),2);

    }


    //findAllByUser 테스트 작성하기
    @Test
    @DisplayName("User 를 넘기면 User와 연관된 Bucket를 List 로 반환됨")
    public void findAllByUser() throws Exception{
        //given
        User user = getUser("1", "email@tesat.com", "testName");
        entityManager.persist(user);

        Bucket bucket = getBucket("버킷내용1", BucketPrivacyStatus.PUBLIC, user);
        SubBucket subBucket1 = getSubBucket("세부목표1");
        SubBucket subBucket2 = getSubBucket("세부목표2");
        bucket.addSubBucket(subBucket1);
        bucket.addSubBucket(subBucket2);
        bucketRepository.save(bucket);

        Bucket bucket2= getBucket("버킷내용2", BucketPrivacyStatus.PUBLIC, user);
        SubBucket subBucket3 = getSubBucket("subBucket1");
        SubBucket subBucket4 = getSubBucket("subBucket2");
        bucket2.addSubBucket(subBucket3);
        bucket2.addSubBucket(subBucket4);

        bucketRepository.save(bucket2);

        User user1 = getUser("2", "email2@tesat.com", "otherName");
        entityManager.persist(user1);

        Bucket bucket3= getBucket("other_user_content", BucketPrivacyStatus.PUBLIC, user1);
        bucketRepository.save(bucket3);

        //when
        List<Bucket> buckets = bucketRepository.findAllByUser(user);
        //then
        assertEquals(buckets.size(), 2);
        assertEquals(true,buckets.stream()
                                 .map(bucket1 -> bucket1.getUser().getUsername())
                                 .allMatch(name ->name.equals("testName")));


    }
    private Bucket getBucket(String content, BucketPrivacyStatus status,User user) {
        return Bucket.builder()
                     .targetDate(LocalDateTime.now())
                     .modifiedTime(LocalDateTime.now())
                     .uploadTime(LocalDateTime.now())
                     .bucketPrivacyStatus(status)
                     .content(content)
                     .user(user)
                     .bucketStatus(BucketStatus.ONGOING)
                     .subBucketList(new ArrayList<>())
                     .build();
    }
    private SubBucket getSubBucket(String content) {
        return SubBucket.builder()
                        .subBucketStatus(SubBucketStatus.ONGOING)
                        .modifiedTime(LocalDateTime.now())
                        .uploadTime(LocalDateTime.now())
                        .content(content)
                        .build();
    }
    private User getUser(String userId, String email, String testName) {
        return User.builder()
                   .userId(userId)
                   .password("test")
                   .email(email)
                   .profileImageUrl("/user/image")
                   .emailVerifiedYn("Y")
                   .username(testName)
                   .roleType(RoleType.USER)
                   .build();
    }

}