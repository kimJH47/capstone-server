package capstone.server.domain.bucket;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SubBucket {

    @Id
    @GeneratedValue
    @Column(name = "detailedBucket_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubBucketStatus subBucketStatus;

    private String content;

    private LocalDateTime uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

}
