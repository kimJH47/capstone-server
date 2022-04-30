package capstone.server.domain.sns;


import capstone.server.domain.User;
import capstone.server.domain.bucket.Bucket;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Memo {
    @Id
    @GeneratedValue
    @Column(name = "memo_id")
    private Long id;
    private String content;
    private LocalDateTime uploadTime;

    @ManyToOne
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

