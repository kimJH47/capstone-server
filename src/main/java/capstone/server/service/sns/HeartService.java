package capstone.server.service.sns;

import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.reactions.Heart;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.sns.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class HeartService {
    private final HeartRepository heartRepository;
    private final BucketRepository bucketRepository;

    private final UserRepository userRepository;

    public boolean addHeart(Long userId, Long bucketId){
        Bucket bucket = bucketRepository.findById(bucketId).orElseThrow(() -> new IllegalArgumentException("버킷 존재하지않음"));

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 존재하지않음"));

        if(isNotAlreadyHeart(user,bucket)){
            heartRepository.save(new Heart(user,bucket));
        }else{
            Heart heart = heartRepository.findByUserAndBucket(user,bucket).get();
            heartRepository.delete(heart);
            //heartRepository.deleteById(target); // <- 넣으면 400에러
        }
        return true;
    }

    private boolean isNotAlreadyHeart(User user, Bucket bucket){
        return heartRepository.findByUserAndBucket(user,bucket).isEmpty();
    }

}
