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
public class SubChallengeParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_sub_challenge_info")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_challenge_id")
    //매핑을 챌린지에 해야하나 아니면 정보에다가 해야하나?
    private SubChallenge subChallenge;

    @Enumerated(EnumType.STRING)
    private SubBucketStatus subBucketStatus;

    public void changeStatus(SubBucketStatus subBucketStatus) {
        this.subBucketStatus = subBucketStatus;
    }


    public static SubChallengeParticipation create(SubChallenge subChallenge, User user, SubBucketStatus bucketStatus) {

        return SubChallengeParticipation.builder()
                                        .subChallenge(subChallenge)
                                        .user(user)
                                        .subBucketStatus(bucketStatus)
                                        .build();
    }

}
