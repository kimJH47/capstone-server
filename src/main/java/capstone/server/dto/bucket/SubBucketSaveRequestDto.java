package capstone.server.dto.bucket;

import capstone.server.domain.bucket.SubBucket;
import capstone.server.domain.bucket.SubBucketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SubBucketSaveRequestDto {

    private Long bucketId;
    private SubBucketStatus subBucketStatus;
    @NotNull
    private String content;

    public SubBucket toEntity() {
        return SubBucket.builder()
                        .content(this.getContent())
                        .subBucketStatus(this.getSubBucketStatus())
                        .build();
    }

}
