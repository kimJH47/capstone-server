package capstone.server.domain.challenge;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ChallengeSearch {

    private Long id;
    private String title;
    private BucketPrivacyStatus challengePrivacyStatus;
    private BucketStatus challengeStatus;
    private Integer maxJoinNum;

}
