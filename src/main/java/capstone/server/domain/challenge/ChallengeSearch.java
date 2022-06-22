package capstone.server.domain.challenge;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeSearch {

    private Long id;
    private String title;
    private BucketPrivacyStatus challengePrivacyStatus;
    private BucketStatus challengeStatus;
    private Integer maxJoinNum;
    private List<String> tagList;

}
