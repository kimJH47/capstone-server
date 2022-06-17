package capstone.server.dto.challenge;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;

import java.time.LocalDateTime;

public class ChallengeResponeDto {


    //참가유저까지 다뿌릴까?
    private String title;
    private String content;
    private BucketPrivacyStatus challengePrivacyStatus;
    private BucketStatus challengeStatus;
    private Integer maxJoinNum;
    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;
}
