package capstone.server.dto.bucket;

import capstone.server.domain.bucket.SubBucket;
import capstone.server.domain.bucket.SubBucketStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@Getter
public class SubBucketSaveRequestDto {

    private Long bucketId;
    private SubBucketStatus subBucketStatus;
    @NotNull
    private String content;
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime uploadTime;
    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime modifiedTime;

    public SubBucket toEntity() {
        return SubBucket.builder()
                        .content(this.getContent())
                        .modifiedTime(this.getModifiedTime())
                        .subBucketStatus(this.getSubBucketStatus())
                        .uploadTime(this.getUploadTime())
                        .build();
    }

}
