package capstone.server.dto;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@Getter
public class BucketResponseDto {


    private String content;
    @NotNull
    private BucketStatus bucketStatus;
    @NotNull
    private BucketPrivacyStatus bucketPrivacyStatus;
    private LocalDateTime uploadTime;
    private LocalDateTime modifiedTime;

}
