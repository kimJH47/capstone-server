package capstone.server.dto.bucket;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@AllArgsConstructor
@Getter
@Builder
public class BucketUpdateDto {


    private String content;
    private BucketStatus bucketStatus;
    private BucketPrivacyStatus bucketPrivacyStatus;


}
