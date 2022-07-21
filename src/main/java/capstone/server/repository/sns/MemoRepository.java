package capstone.server.repository.sns;
import capstone.server.domain.User;
import capstone.server.domain.bucket.reactions.Comment;
import capstone.server.domain.bucket.reactions.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface MemoRepository extends JpaRepository<Memo,Long> {
    Optional<Memo> findByUserAndBucket(User user, Comment comment);
}
