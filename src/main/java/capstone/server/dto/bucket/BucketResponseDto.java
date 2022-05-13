package capstone.server.dto.bucket;

import capstone.server.domain.bucket.BucketPrivacyStatus;
import capstone.server.domain.bucket.BucketStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime uploadTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime modifiedTime;

}
