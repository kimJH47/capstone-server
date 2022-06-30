package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.dto.bucket.*;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketImageRepository;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.bucket.SubBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final BucketRepository bucketRepository;

    private final UserRepository userRepository;

    private final SubBucketRepository subBucketRepository;

    private final BucketImageRepository bucketImageRepository;


    @Transactional
    public Long saveBucket(BucketSaveRequestDto requestDto) {
        System.out.println("==============");
        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 유저데이터가 없습니다"));
        System.out.println("findUser = " + findUser.getUsername());
        Bucket bucket = requestDto.toEntity();
        bucket.changeUser(findUser);
        requestDto.getSubBucketSaveRequestDtoList()
                  .stream()
                  .map(SubBucketSaveRequestDto::toEntity)
                  .forEach(bucket::addSubBucket);

        Bucket saveBucket = bucketRepository.save(bucket);
        return saveBucket.getId();
    }
    //버킷리스트 중 하나를 조회할때 사용되는 로직(세부목표와 함깨 반환됨)
    @Transactional(readOnly = true)
    public BucketResponseDto findOne(Long id) {

        Bucket findBucket = bucketRepository.findById(id)
                                            .orElseThrow(() -> new IllegalArgumentException("테이블에 버킷이 없습니다"));
        return BucketResponseDto.create(findBucket);
    }

    /**
     *
     * @param userId 버킷작성자의 유저 ID
     * @return '세부목표' 가 함깨 포함된 버킷리스트를 Dto 로 반환, 세부목표를 반환을 같이 반환안할려다가 하는게 좋을꺼 같아서 반환함
     */
    @Transactional(readOnly = true)
    public List<BucketResponseDto> findBucketsByUserId(Long userId) {
        User findUser = userRepository.findById(userId)
                                           .orElseThrow(() -> new IllegalArgumentException("테이블에 유저가 없습니다"));
        List<Bucket> buckets = bucketRepository.findAllByUser(findUser);

        return buckets.stream()
               .map(BucketResponseDto::create)
               .collect(Collectors.toList());


    }

    @Transactional
    public void updateBucketContent(BucketContentUpdateDto bucketUpdateDto,Long bucketId) {
        Bucket bucket = bucketRepository.findById(bucketId)
                                        .orElseThrow(() -> new IllegalArgumentException("테이블에 버킷이 없습니다"));
        bucket.changeContent(bucketUpdateDto.getContent());
        bucket.setModifiedTime(bucketUpdateDto.getUpdateTime());
    }

    @Transactional
    public void updateBucketStatus(BucketStatusUpdateDto bucketStatusUpdateDto, Long bucketId) {
        Bucket bucket = bucketRepository.findById(bucketId)
                                        .orElseThrow(() -> new IllegalArgumentException("테이블에 버킷이 없습니다"));
        bucket.changeStatus(bucketStatusUpdateDto.getStatus());
        bucket.setModifiedTime(bucketStatusUpdateDto.getUpdateTime());

    }


    public void updateBucket(BucketUpdateDto updateDto, Long BucketId) {
        Bucket bucket = bucketRepository.findById(BucketId)
                                        .orElseThrow(() -> new IllegalArgumentException("테이블에 버킷이 없습니다"));
        bucket.update(updateDto);

    }
}
