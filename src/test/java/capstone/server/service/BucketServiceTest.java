package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.bucket.*;
import capstone.server.dto.bucket.*;
import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@Transactional
class BucketServiceTest {

    @InjectMocks
    private BucketService bucketService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BucketRepository bucketRepository;

    @BeforeEach
    public void setUp() {

//        Stream.of(1, 2, 3, 4, 5)
//              .map(integer -> User.builder()
//                                  .email("email@naver.com")
//                                  .username("test" + integer)
//                                  .roleType(RoleType.USER)
//                                  .providerType(ProviderType.KAKAO)
//                                  .profileImageUrl("/test/image")
//                                  .emailVerifiedYn("Y")
//                                  .password("testPass")
//                                  .userId(String.valueOf(integer))
//                                  .build())
//              .map(user -> userRepository.save(user))
//              .collect(Collectors.toList());

        User user = User.builder()
                        .email("email@naver.com")
                        .username("testname")
                        .roleType(RoleType.USER)
                        .providerType(ProviderType.KAKAO)
                        .profileImageUrl("/test/image")
                        .emailVerifiedYn("Y")
                        .password("testPass")
                        .userId("1")
                        .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
    }
    @Test
    @DisplayName("세부목표가 포함된 BucketSaveRequestDto를 받으면 버킷등록이 완료되어야함")
    public void 버킷등록() throws Exception {

        //given
        //초기저장 시에는 SubBucketDto 에 버킷ID를 보내지 않는다.
        List<SubBucketSaveRequestDto> list =new ArrayList<>();
        for(int i = 0;i<5;i++) {
            list.add(SubBucketSaveRequestDto.builder()
                                            .content("세부목표1")
                                            .subBucketStatus(SubBucketStatus.ONGOING)
                                            .build());
        }
        BucketSaveRequestDto dto = BucketSaveRequestDto.builder()
                                                       .userId(1L)
                                                       .bucketPrivacyStatus(BucketPrivacyStatus.PUBLIC)
                                                       .content("버킷내용")
                                                       .subBucketSaveRequestDtoList(list)
                                                       .bucketStatus(BucketStatus.ONGOING)
                                                       .build();
        User user = userRepository.findById(1L)
                                  .orElseThrow(() -> new IllegalArgumentException("Mock 유저 존재하지않음"));
        Bucket bucket = dto.toEntity();
        dto.getSubBucketSaveRequestDtoList()
           .stream()
           .map(SubBucketSaveRequestDto::toEntity)
           .forEach(bucket::addSubBucket);
        bucket.changeUser(user);

        //when
        when(bucketRepository.save(any(Bucket.class))).thenReturn(bucket);
        when(bucketRepository.findById(anyLong())).thenReturn(Optional.of(bucket));
        bucketService.saveBucket(dto);
        //then
        Bucket findBucket = bucketRepository.findById(1L)
                                            .orElseThrow(() -> new IllegalArgumentException("Mock Bucket 이 존재하지 않음"));
        verify(bucketRepository).save(bucket);
        //버킷 저장 검증
        Assertions.assertThat(findBucket)
                  .isEqualTo(bucket);
        List<SubBucket> subBucketList = bucket.getSubBucketList();
        //세부목표 저장 검증
        Assertions.assertThat(subBucketList.size())
                  .isEqualTo(5);
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