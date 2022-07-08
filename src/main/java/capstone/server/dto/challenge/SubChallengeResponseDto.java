package capstone.server.dto.challenge;


import capstone.server.domain.bucket.SubBucketStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class SubChallengeResponseDto {

    private String content;
    private SubBucketStatus bucketStatus;
}
