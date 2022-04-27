package capstone.server.domain.bucket;

import capstone.server.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Bucket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "bucket_name")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private BucketStatus bucketStatus;

    @Column
    private LocalDateTime uploadTime;



}
