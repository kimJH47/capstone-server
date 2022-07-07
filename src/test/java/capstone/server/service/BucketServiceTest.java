package capstone.server.service;

import capstone.server.domain.User;
import capstone.server.domain.bucket.*;
import capstone.server.dto.bucket.*;
import capstone.server.oauth.entity.ProviderType;
import capstone.server.oauth.entity.RoleType;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
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
    public void init() {
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
        BucketSaveRequestDto dto = getBucketSaveRequestDto("버킷내용");
        User user = userRepository.findById(1L)
                                  .orElseThrow(() -> new IllegalArgumentException("Mock 유저 존재하지않음"));

        Bucket bucket = dto.toEntity();
        dto.getSubBucketSaveRequestDtoList()
           .stream()
           .map(SubBucketSaveRequestDto::toEntity)
           .forEach(bucket::addSubBucket);

        bucket.changeUser(user);


        given(bucketRepository.save(any())).willReturn(bucket);
        given(bucketRepository.findById(anyLong())).willReturn(Optional.of(bucket)); //검증을 위한 버킷 조회시 행위 지정

        //when
        bucketService.saveBucket(dto);
        //then
        Bucket findBucket = bucketRepository.findById(1L)
                                            .orElseThrow(() -> new IllegalArgumentException("Mock Bucket 이 존재하지 않음"));
        then(bucketRepository).should(times(1))
                              .save(any(Bucket.class));
        //버킷 저장 검증
        assertThat(findBucket)
                  .isEqualTo(bucket);
        List<SubBucket> subBucketList = bucket.getSubBucketList();
        //세부목표 저장 검증
        assertThat(subBucketList.size())
                  .isEqualTo(5);
    }



    @Test
    @DisplayName("버킷아이디 하나를 보내면 버킷단건조회가 완료되어야함")
    public void 버킷단건조회() throws Exception {
        //given
        BucketSaveRequestDto saveDto = getBucketSaveRequestDto("버킷내용");
        User user = userRepository.findById(1L)
                                  .orElseThrow(() -> new IllegalArgumentException("Mock 유저 존재하지않음"));

        Bucket bucket = saveDto.toEntity();
        saveDto.getSubBucketSaveRequestDtoList()
           .stream()
           .map(SubBucketSaveRequestDto::toEntity)
           .forEach(bucket::addSubBucket);
        bucket.changeUser(user);

        given(bucketRepository.findById(anyLong())).willReturn(Optional.of(bucket)); //검증을 위한 버킷 조회시 행위 지정

        //when
        BucketResponseDto findDto = bucketService.findOne(1L);

        //then
        then(bucketRepository).should(times(1))
                              .findById(1L);

        assertThat(findDto.getContent()).isEqualTo(saveDto.getContent());
        assertThat(findDto.getSubBucketList().size()).isEqualTo(saveDto.getSubBucketSaveRequestDtoList().size());


    }

    @Test
    @DisplayName("유저 아이디를 받으면 유저가 가지고있는 버킷이 전부 조회되어야한다")
    public void 버킷전체조회_테스트() throws Exception{
        //given
        BucketSaveRequestDto saveDto = getBucketSaveRequestDto("버킷내용1");
        User user = userRepository.findById(1L)
                                  .orElseThrow(() -> new IllegalArgumentException("Mock 유저 존재하지않음"));

        Bucket bucket1 = saveDto.toEntity();
        saveDto.getSubBucketSaveRequestDtoList()
               .stream()
               .map(SubBucketSaveRequestDto::toEntity)
               .forEach(bucket1::addSubBucket);
        bucket1.changeUser(user);

        BucketSaveRequestDto saveDto2 = getBucketSaveRequestDto("버킷내용2");
        Bucket bucket2 = saveDto2.toEntity();
        saveDto2.getSubBucketSaveRequestDtoList()
               .stream()
               .map(SubBucketSaveRequestDto::toEntity)
               .forEach(bucket2::addSubBucket);
        bucket2.changeUser(user);

        List<Bucket> buckets = new ArrayList<>();
        buckets.add(bucket1);
        buckets.add(bucket2);

        given(bucketRepository.findAllByUser(any(User.class))).willReturn(buckets); //검증을 위한 버킷 조회시 행위 지정

        //when
        List<BucketResponseDto> findBuckets = bucketService.findBucketsByUserId(1L);
        //then
        then(bucketRepository).should(times(1))
                              .findAllByUser(any(User.class));
        assertThat(findBuckets.size()).isEqualTo(2);

        assertThat(findBuckets.stream()
                              .map(BucketResponseDto::getContent)
                              .collect(Collectors.toList())).containsExactlyInAnyOrder("버킷내용1", "버킷내용2");

    }


    @Test
    @DisplayName("재작성")
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

    //테스트용 dto
    private BucketSaveRequestDto getBucketSaveRequestDto(String content) {
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
                                                       .content(content)
                                                       .subBucketSaveRequestDtoList(list)
                                                       .bucketStatus(BucketStatus.ONGOING)
                                                       .build();
        return dto;
    }

}