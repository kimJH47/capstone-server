package capstone.server.domain.bucket;

import capstone.server.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Bucket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "bucket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String content;

    @Enumerated(EnumType.STRING)
    private BucketStatus bucketStatus;
    @Enumerated(EnumType.STRING)
    private BucketPrivacyStatus bucketPrivacyStatus;

    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;


    //단방향 , setter 사용x
    public void changeUser(User user) {
        this.user = user;
    }

}
