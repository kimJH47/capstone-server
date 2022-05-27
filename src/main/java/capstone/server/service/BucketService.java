package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.dto.bucket.BucketResponseDto;
import capstone.server.dto.bucket.BucketSaveRequestDto;
import capstone.server.dto.bucket.BucketUpdateDto;
import capstone.server.repository.UserRepository;
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


    @Transactional
    public void saveBucket(BucketSaveRequestDto requestDto) {

        Bucket bucket = requestDto.toEntity();
        User findUser = userRepository.findById(requestDto.getUserId())
                                      .orElseThrow(() -> new IllegalArgumentException("테이블에 유저데이터가 없습니다"));
        bucket.changeUser(findUser);
        bucketRepository.save(bucket);
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
    public void updateBucketStatus(BucketUpdateDto bucketUpdateDto) {

    }

}
