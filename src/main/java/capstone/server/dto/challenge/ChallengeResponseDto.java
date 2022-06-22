package capstone.server.dto.challenge;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ChallengeResponseDto {


    private String title;
    private String content;
    private BucketPrivacyStatus challengePrivacyStatus;
    private BucketStatus challengeStatus;
    private Integer maxJoinNum;
    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;

    public ChallengeResponseDto(Challenge challenge) {
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.challengePrivacyStatus = challenge.getChallengePrivacyStatus();
        this.challengeStatus = challenge.getChallengeStatus();
        this.maxJoinNum = challenge.getMaxJoinNum();
        this.uploadTime = challenge.getUploadTime();
        this.modifiedTime = challenge.getModifiedTime();
    }
}
