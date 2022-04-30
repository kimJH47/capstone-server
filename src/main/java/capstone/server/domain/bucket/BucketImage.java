package capstone.server.domain.bucket;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BucketImage {

    @Id
    @GeneratedValue
    @Column(name = "bucket_image_id")
    private Long id;

    private LocalDateTime uploadTime;

    private String location;



}
