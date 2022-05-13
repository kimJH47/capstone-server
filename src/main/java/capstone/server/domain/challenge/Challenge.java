package capstone.server.domain.challenge;


import capstone.server.domain.User;
import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Challenge {

    @Id
    @GeneratedValue
    @Column(name = "challenge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createAt;


    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private BucketPrivacyStatus challengePrivacyStatus;
    @Enumerated(EnumType.STRING)
    private BucketStatus challengeStatus;
    private Integer maxJoinNum;
    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;

    public void changeUser(User user) {
        this.createAt = user;
    }


}
