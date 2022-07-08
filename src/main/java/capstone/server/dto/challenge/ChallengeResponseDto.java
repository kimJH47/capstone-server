package capstone.server.dto.challenge;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.Challenge;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChallengeResponseDto {

    private Long id;
    private String title;
    private String content;
    private BucketPrivacyStatus challengePrivacyStatus;
    private BucketStatus challengeStatus;
    private Integer maxJoinNum;
    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;
    private List<SubChallengeResponseDto> subChallengeResponseDtoList;
    private LocalDateTime targetDate;
    private List<String> tagList;

    public ChallengeResponseDto(Challenge challenge) {
        this.id = getId();
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.challengePrivacyStatus = challenge.getChallengePrivacyStatus();
        this.challengeStatus = challenge.getChallengeStatus();
        this.maxJoinNum = challenge.getMaxJoinNum();
        this.uploadTime = challenge.getUploadTime();
        this.modifiedTime = challenge.getModifiedTime();
        this.targetDate = challenge.getTargetDate();


    }

    public void changeSubCallengeDtos(List<SubChallengeResponseDto> dtos) {
        this.subChallengeResponseDtoList = dtos;
    }
}
