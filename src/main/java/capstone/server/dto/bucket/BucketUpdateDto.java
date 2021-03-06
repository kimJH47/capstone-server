package capstone.server.dto.bucket;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
@Builder
public class BucketUpdateDto {


    private String content;
    private BucketStatus bucketStatus;
    private BucketPrivacyStatus bucketPrivacyStatus;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime modifiedTime;


}
