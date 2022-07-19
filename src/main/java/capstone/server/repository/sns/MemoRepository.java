package capstone.server.repository.sns;
import capstone.server.domain.User;
import capstone.server.domain.bucket.reactions.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findByUserAndBucket(User user, Comment comment);
}
