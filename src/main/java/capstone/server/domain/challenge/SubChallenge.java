package capstone.server.domain.challenge;


import capstone.server.domain.bucket.SubBucketStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SubChallenge {

    @Id
    @GeneratedValue
    @Column(name = "sub_challenge_id")
    private Long id;

    private SubBucketStatus subBucketStatus;

    private String content;

    private LocalDateTime uploadTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;
}
