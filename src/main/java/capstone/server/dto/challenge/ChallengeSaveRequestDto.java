package capstone.server.dto.challenge;


import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import capstone.server.domain.challenge.Challenge;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ChallengeSaveRequestDto {

    @NotNull
    private Long userId;
    @NotNull
    @Size(max = 20)
    private String title;
    @NotNull
    private String content;
    @NotNull
    @Size(max = 10)
    private Integer maxJoinNum;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime uploadTime;

    public Challenge toEntity() {
        /**
         * 챌린지 생성 기본값
         * - 챌린지 상태 : 진행중
         * - 챌린지 공개여부 : 공개
         */
        return Challenge.builder()
                        .title(this.getTitle())
                        .content(this.getContent())
                        .uploadTime(this.getUploadTime())
                        .modifiedTime(this.getUploadTime())
                        .maxJoinNum(this.getMaxJoinNum())
                        .challengeStatus(BucketStatus.ONGOING)
                        .challengePrivacyStatus(BucketPrivacyStatus.PUBLIC)
                        .build();
    }
}
