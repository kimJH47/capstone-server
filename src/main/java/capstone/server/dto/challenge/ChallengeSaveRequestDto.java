package capstone.server.dto.challenge;


import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.Challenge;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChallengeSaveRequestDto {

    @NotNull
    private Long userId;
    @NotBlank
    private String title;
    @NotNull
    private String content;
    @NotNull
    @Max(value = 10)
    private Integer maxJoinNum;
    private BucketPrivacyStatus challengePrivacyStatus;
    private List<String> tagList;
    private List<SubChallengeSaveRequestDto> subChallengeSaveRequestDtoList;
    private LocalDateTime targetDate;

    public Challenge toEntity() {
        /**
         * 챌린지 생성 기본값
         * - 챌린지 상태 : BucketStatus.ONGOING
         */
        return Challenge.builder()
                        .title(this.getTitle())
                        .content(this.getContent())
                        .maxJoinNum(this.getMaxJoinNum())
                        .challengeStatus(BucketStatus.ONGOING)
                        .challengePrivacyStatus(this.getChallengePrivacyStatus())
                        .tagList(new ArrayList<>())
                        .targetDate(targetDate)
                        .build();
    }
}
