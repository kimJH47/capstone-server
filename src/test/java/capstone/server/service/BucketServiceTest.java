package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.dto.bucket.BucketContentUpdateDto;
import capstone.server.dto.bucket.BucketResponseDto;
import capstone.server.dto.bucket.BucketStatusUpdateDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest
@Transactional
class BucketServiceTest {

    @Autowired
    private BucketService bucketService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BucketRepository bucketRepository;

    @BeforeEach
    public void 테스트유저_생성() {

        List<User> collect = Stream.of(1, 2, 3, 4, 5)
                                   .map(integer -> User.builder()
                                                       .email("email@naver.com")
                                                       .name("test" + integer)
                                                       .build())
                                   .map(user -> userRepository.save(user))
                                   .collect(Collectors.toList());

    }

    @Test
    @DisplayName("버킷등록이 완료되어야함")

    public void 버킷등록() throws Exception {
        //given
        //when
        //then
    }

    @Test
    @DisplayName("버킷아이디 하나를 보내면 버킷조회가 완료되어야함")
    public void 버킷조회() throws Exception {
        //given
        //when
        //then
    }

    @Test
    public void 버킷전체조회_테스트() throws Exception{
        //given
        User user = userRepository.findById(1L)
                                  .get();

        for (int i = 0; i < 10; i++) {
            Bucket bucket1 = Bucket.builder()
                                   .content("버킷"+i)
                                   .bucketStatus(BucketStatus.ONGOING)
                                   .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                   .user(user)
                                   .uploadTime(LocalDateTime.now())
                                   .modifiedTime(LocalDateTime.now())
                                   .build();
            bucketRepository.save(bucket1);
        }

        //when
        List<BucketResponseDto> bucketsByUserId = bucketService.findBucketsByUserId(1L);
        //then
        Assertions.assertThat(bucketsByUserId.size())
                  .isEqualTo(10);

    }


    @Test
    public void 컨탠츠업데이트_테스트() throws Exception{
        //given
        User user = userRepository.findById(1L)
                                  .get();

        Bucket bucket1 = Bucket.builder()
                               .content("버킷")
                               .bucketStatus(BucketStatus.ONGOING)
                               .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                               .user(user)
                               .uploadTime(LocalDateTime.now())
                               .modifiedTime(LocalDateTime.now())
                               .build();
        bucketRepository.save(bucket1);
        //when
        bucketService.updateBucketContent(BucketContentUpdateDto.builder()
                                                                .content("수정")
                                                                .updateTime(LocalDateTime.now())
                                                                .build(), 1L);
        //then
        bucketRepository.findById(1L)
                        .map(Bucket::getContent)
                        .stream()
                        .allMatch(s -> s.equals("수정"));
    }

    @Test
    public void 버킷상태_업데이트() throws Exception{
        //given
        User user = userRepository.findById(1L)
                                  .get();

        Bucket bucket1 = Bucket.builder()
                               .content("버킷")
                               .bucketStatus(BucketStatus.ONGOING)
                               .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                               .user(user)
                               .uploadTime(LocalDateTime.now())
                               .modifiedTime(LocalDateTime.now())
                               .build();
        bucketRepository.save(bucket1);
        //when
        bucketService.updateBucketStatus(BucketStatusUpdateDto.builder()
                                                               .status(BucketStatus.COMPLETED)
                                                               .updateTime(LocalDateTime.now())
                                                               .build(), 1L);
        //then
        bucketRepository.findById(1L)
                        .map(Bucket::getBucketStatus)
                        .stream()
                        .allMatch(bucketStatus -> bucketStatus.equals(BucketStatus.COMPLETED));

    }

}