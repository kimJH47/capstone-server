package capstone.server.domain.bucket;

import capstone.server.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Bucket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "bucket_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String content;

    @Enumerated(EnumType.STRING)
    private BucketStatus bucketStatus;

    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;



}
