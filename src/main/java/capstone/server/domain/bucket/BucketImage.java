package capstone.server.domain.bucket;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BucketImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucket_image_id")
    private Long id;

    private LocalDateTime uploadTime;

    private String location;



}
