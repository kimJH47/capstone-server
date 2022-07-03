package capstone.server.dto.challenge;


import capstone.server.domain.bucket.SubBucketStatus;
import capstone.server.domain.challenge.SubChallenge;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class SubChallengeSaveRequestDto {

    //최초 저장시 id 는 null
    private Long challengeId;
    private SubBucketStatus subBucketStatus;
    private String content;

    public SubChallenge toEntity() {
        return new SubChallenge(this.content);
    }
}
