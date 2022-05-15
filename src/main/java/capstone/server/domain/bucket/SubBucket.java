package capstone.server.domain.bucket;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SubBucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailedBucket_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubBucketStatus subBucketStatus;

    private String content;

    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    public void changeBucket(Bucket bucket) {
        this.bucket = bucket;
    }

}
