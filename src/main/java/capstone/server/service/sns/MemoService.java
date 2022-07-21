package capstone.server.service.sns;

import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import capstone.server.domain.bucket.reactions.Comment;
import capstone.server.domain.bucket.reactions.Memo;
import capstone.server.repository.UserRepository;
import capstone.server.repository.bucket.BucketRepository;
import capstone.server.repository.sns.CommentRepository;
import capstone.server.repository.sns.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemoService {
    private final MemoRepository memoRepository;
    private final BucketRepository bucketRepository;
    private final UserRepository userRepository;

    public boolean addMemo(Long userId,Long bucketId,String content){
        Bucket bucket = bucketRepository.findById(bucketId).orElseThrow(() -> new IllegalArgumentException("버킷 존재하지않음"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 존재하지않음"));

        memoRepository.save(new Memo(user,bucket,content));

        return true;
    }
}
