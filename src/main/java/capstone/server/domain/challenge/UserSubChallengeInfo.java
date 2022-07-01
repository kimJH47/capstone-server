package capstone.server.domain.challenge;


import capstone.server.domain.User;
import capstone.server.domain.bucket.SubBucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserSubChallengeInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_sub_challenge_info")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Enumerated(EnumType.STRING)
    private SubBucketStatus subBucketStatus;

    public void changeStatus(SubBucketStatus subBucketStatus) {
        this.subBucketStatus = subBucketStatus;
    }
}
