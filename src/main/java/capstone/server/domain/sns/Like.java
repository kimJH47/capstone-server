package capstone.server.domain.sns;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;
    private LocalDateTime uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
