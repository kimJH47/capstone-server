package capstone.server.dto.challenge;


import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.Challenge;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
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
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime uploadTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime modifiedTime;

    private List<String> tagList;
    public Challenge toEntity() {
        /**
         * 챌린지 생성 기본값
         * - 챌린지 상태 : 진행중
         */
        return Challenge.builder()
                        .title(this.getTitle())
                        .content(this.getContent())
                        .uploadTime(this.getUploadTime())
                        .modifiedTime(this.getUploadTime())
                        .maxJoinNum(this.getMaxJoinNum())
                        .challengeStatus(BucketStatus.ONGOING)
                        .challengePrivacyStatus(this.getChallengePrivacyStatus())
                        .build();
    }
}
