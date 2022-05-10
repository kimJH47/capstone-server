package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.dto.BucketResponseDto;
import capstone.server.dto.BucketSaveRequestDto;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
