package capstone.server.service;


import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.SubBucket;
import capstone.server.dto.bucket.SubBucketResponseDto;
import capstone.server.dto.bucket.SubBucketSaveRequestDto;
import capstone.server.dto.bucket.SubBucketUpdateDto;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.bucket.SubBucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubBucketService {

    private final BucketRepository bucketRepository;
    private final SubBucketRepository subBucketRepository;


    /**
     *  : 버킷이 존재하는 상태에서 세부목표를 추가로 생성할때 사용되는 로직
     */
    @Transactional
    public void saveSubBucket(SubBucketSaveRequestDto requestDto) {
        Long bucketId = requestDto.getBucketId();
        Bucket findBucket = bucketRepository.findById(bucketId)
                                            .orElseThrow(() -> new IllegalArgumentException("버킷이 테이블에 존재하지 않습니다"));
        SubBucket subBucket = requestDto.toEntity();
        subBucket.changeBucket(findBucket);
        subBucketRepository.save(subBucket);
    }
    //정적 dto 생성메서드 or 생성자로 넘길까...
    @Transactional(readOnly = true)
    public List<SubBucketResponseDto> findByBucketId(Long id) {
        List<SubBucketResponseDto> responseDtos = subBucketRepository.findByBucketId(id)
                                                                .stream()
                                                                .map(subBucket -> SubBucketResponseDto.builder()
                                                                                                      .subBucketStatus(subBucket.getSubBucketStatus())
                                                                                                      .content(subBucket.getContent())
                                                                                                      .modifiedTime(subBucket.getModifiedTime())
                                                                                                      .uploadTime(subBucket.getUploadTime())
                                                                                                      .build())
                                                                .collect(Collectors.toList());

//        if (responseDtos.isEmpty()) {
//            new IllegalArgumentException("테이블에 세부목표가 존재하지 않습니다");
//        }
        return responseDtos;

    }

    @Transactional
    public void updateSubBucket(SubBucketUpdateDto updateSubBucketDto) {
        SubBucket subBucket = subBucketRepository.findById(updateSubBucketDto.getSubBucketId())
                                                 .orElseThrow(() -> new IllegalArgumentException("세부목표가 테이블에 존재하지 않습니다"));
        subBucket.update(updateSubBucketDto);

    }

}
