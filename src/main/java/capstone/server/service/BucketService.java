package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.dto.bucket.*;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketImageRepository;
import capstone.server.repository.bucket.BucketRepository;
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

    private final BucketImageRepository bucketImageRepository;


    @Transactional
    public Long saveBucket(BucketSaveRequestDto requestDto) {

        Bucket bucket = requestDto.toEntity();
        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 유저데이터가 없습니다"));
        bucket.changeUser(findUser);
        return bucketRepository.save(bucket)
                               .getId();
    }

    @Transactional(readOnly = true)
    public BucketResponseDto findById(Long id) {
        Bucket findBucket = bucketRepository.findById(id)
                                            .orElseThrow(() -> new IllegalArgumentException("테이블에 버킷이 없습니다"));
        return BucketResponseDto.builder()
                                .content(findBucket.getContent())
                                .bucketStatus(findBucket.getBucketStatus())
                                .bucketPrivacyStatus(findBucket.getBucketPrivacyStatus())
                                .modifiedTime(findBucket.getModifiedTime())
                                .uploadTime(findBucket.getUploadTime())
                                .build();

    }

    @Transactional(readOnly = true)
    public List<BucketResponseDto> findBucketsByUserId(Long userId) {
        User findUser = userRepository.findById(userId)
                                           .orElseThrow(() -> new IllegalArgumentException("테이블에 유저가 없습니다"));
        List<Bucket> buckets = bucketRepository.findAllByUser(findUser);
        return buckets.stream()
                      .map(bucket -> BucketResponseDto.create(bucket))
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
