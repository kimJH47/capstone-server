package capstone.server.repository.bucket;

import capstone.server.domain.bucket.BucketImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketImageRepository extends JpaRepository<BucketImage, Long> {

}
