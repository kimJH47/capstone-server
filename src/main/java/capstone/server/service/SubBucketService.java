package capstone.server.service;


import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.SubBucket;
import capstone.server.dto.SubBucketSaveRequestDto;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.bucket.SubBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SubBucketService {

    private final BucketRepository bucketRepository;
    private final SubBucketRepository subBucketRepository;

    @Transactional
    public void saveSubBucket(SubBucketSaveRequestDto requestDto) {
        Long bucketId = requestDto.getBucketId();
        Bucket findBucket = bucketRepository.findById(bucketId)
                                        .orElseThrow(() -> new IllegalArgumentException("버킷이 테이블에 존재하지 않습니다"));
        SubBucket subBucket = requestDto.toEntity();
        subBucket.changeBucket(findBucket);
        subBucketRepository.save(subBucket);
    }
}
