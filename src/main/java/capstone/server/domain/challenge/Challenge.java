package capstone.server.domain.challenge;


import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Challenge {

    @Id
    @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private BucketPrivacyStatus challengePrivacyStatus;
    @Enumerated(EnumType.STRING)
    private BucketStatus challengeStatus;

    private LocalDateTime uploadTime;




}
