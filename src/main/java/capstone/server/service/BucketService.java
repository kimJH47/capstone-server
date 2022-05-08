package capstone.server.service;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
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




}
